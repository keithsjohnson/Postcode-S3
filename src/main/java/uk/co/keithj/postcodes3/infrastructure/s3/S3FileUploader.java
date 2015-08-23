package uk.co.keithj.postcodes3.infrastructure.s3;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.IOUtils;

@Component
public class S3FileUploader {

	@Autowired
	private TransferManager s3TransferManager;

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
