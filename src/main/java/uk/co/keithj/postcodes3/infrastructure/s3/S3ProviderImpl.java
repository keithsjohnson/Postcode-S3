package uk.co.keithj.postcodes3.infrastructure.s3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.IOUtils;

import uk.co.keithj.postcodes3.api.StoreProvider;

@Component
public class S3ProviderImpl implements StoreProvider {

	@Autowired
	private TransferManager s3TransferManager;

	@Autowired
	private AmazonS3 amazonS3;

	@Override
	public void retrieve(String message) {
		System.out.println("S3 RETREIVE: " + message);

		S3Object s3object = amazonS3.getObject(new GetObjectRequest("postcodelocationfinderfiles", "postcodes.csv"));

		System.out.println("Content-Type: " + s3object.getObjectMetadata().getContentType());
		displayTextInputStream(s3object.getObjectContent());

		// Get a range of bytes from an object.

		GetObjectRequest rangeObjectRequest = new GetObjectRequest("postcodelocationfinderfiles", "postcodes.csv");
		rangeObjectRequest.setRange(0, 18);
		S3Object objectPortion = amazonS3.getObject(rangeObjectRequest);

		System.out.println("Printing bytes retrieved.");
		displayTextInputStream(objectPortion.getObjectContent());

		amazonS3.deleteObject("postcodelocationfinderfiles", "postcodes.csv");
	}

	private static void displayTextInputStream(InputStream input) {
		// Read one text line at a time and display.
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		while (true) {

			String line;
			try {
				line = reader.readLine();
			} catch (IOException e) {
				throw new RuntimeException("displayTextInputStream", e);
			}
			if (line == null)
				break;

			System.out.println("    " + line);
		}
		System.out.println();
	}

	@Override
	public void store(String key, String filename) {
		PutObjectRequest putObjectRequest = getPutObjectRequest(key, filename);

		long startTime = System.currentTimeMillis();
		Upload upload = s3TransferManager.upload(putObjectRequest);

		waitForUploadToComplete(upload);
		System.out.println("After " + upload.getDescription() + ", State: " + upload.getState() + ", Duration: "
				+ (System.currentTimeMillis() - startTime) + "ms.");
	}

	private PutObjectRequest getPutObjectRequest(String key, String filename) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(getFileContentLength(filename));

		PutObjectRequest putObjectRequest = null;
		try {
			putObjectRequest = new PutObjectRequest("postcodelocationfinderfiles", key, new FileInputStream(filename),
					metadata);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found: " + filename, e);
		}
		return putObjectRequest;
	}

	private void waitForUploadToComplete(Upload upload) {
		AmazonClientException amazonClientException = null;
		try {
			amazonClientException = upload.waitForException();
		} catch (InterruptedException e1) {
			throw new RuntimeException("Thread.sleep interruped: ", e1);
		}
		if (amazonClientException != null) {
			amazonClientException.printStackTrace();
		}
	}

	private int getFileContentLength(String filename) {
		try {
			FileInputStream fileInputStream = new FileInputStream(filename);
			byte[] contentBytes = IOUtils.toByteArray(fileInputStream);

			return contentBytes.length;
		} catch (Exception e) {
			throw new RuntimeException("Cannot get information for file: " + filename, e);
		}

	}
}
