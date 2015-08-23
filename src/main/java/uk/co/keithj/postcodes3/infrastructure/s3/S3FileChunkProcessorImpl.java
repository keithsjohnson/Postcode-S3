package uk.co.keithj.postcodes3.infrastructure.s3;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import uk.co.keithj.postcodes3.infrastructure.s3.S3ProviderImpl.S3FileInfo;

@Component
public class S3FileChunkProcessorImpl {

	@Autowired
	private AmazonS3 amazonS3;

	@Autowired
	private S3FileReader s3FileReader;

	public void getFileFromS3InChunks(S3FileInfo s3FileInfo) {
		GetObjectRequest rangeObjectRequest = new GetObjectRequest(s3FileInfo.getBucketName(),
				s3FileInfo.getObjectKey());

		int chunkSize = 1000;
		int startPosition = 0;
		boolean moreFileData = true;
		String remainingLine = "";
		while (moreFileData) {

			System.out.println("startPosition: " + startPosition + ", remainingLine: " + remainingLine);

			// Get a range of bytes from an object.
			rangeObjectRequest.setRange(startPosition, startPosition + chunkSize - 1);
			S3Object objectPortion = amazonS3.getObject(rangeObjectRequest);

			int contentLength = (int) objectPortion.getObjectMetadata().getContentLength();
			System.out.println("contentLength: " + contentLength);

			if (contentLength < chunkSize) {
				moreFileData = false;
			}

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(objectPortion.getObjectContent()));

			remainingLine = s3FileReader.processInputStreamIntoTextLines(bufferedReader, 0, contentLength,
					remainingLine);

			startPosition += chunkSize;
		}
	}

}
