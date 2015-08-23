package uk.co.keithj.postcodes3.infrastructure.s3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.co.keithj.postcodes3.api.StoreProvider;

@Component
public class S3ProviderImpl implements StoreProvider {

	@Autowired
	private AmazonS3 amazonS3;

	@Autowired
	private S3FileChunkProcessorImpl s3FileChunkProcessorImpl;

	@Autowired
	private S3FileUploader s3FileUploader;

	@Override
	public void retrieve(String message) {
		System.out.println("S3 RETRIEVE: " + message);

		S3FileInfo s3FileInfo = getS3FileInfoFromMessage(message);

		s3FileChunkProcessorImpl.getFileFromS3InChunks(s3FileInfo);

		amazonS3.deleteObject(s3FileInfo.getBucketName(), s3FileInfo.getObjectKey());
	}

	private S3FileInfo getS3FileInfoFromMessage(String message) {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode messageJsonNode = null;
		try {
			messageJsonNode = mapper.readTree(message);
		} catch (Exception e) {
			throw new RuntimeException("error reading Json Tree ", e);
		}

		JsonNode recordsJsonNodeArray = messageJsonNode.findValue("Records");

		S3FileInfo s3FileInfo = null;
		if (recordsJsonNodeArray.isArray()) {
			for (final JsonNode objNode : recordsJsonNodeArray) {
				String s3BucketName = null;
				String s3ObjectKey = null;
				s3BucketName = objNode.path("s3").path("bucket").path("name").asText();
				System.out.println(s3BucketName);
				s3ObjectKey = objNode.path("s3").path("object").path("key").asText();
				System.out.println(s3ObjectKey);
				s3FileInfo = new S3FileInfo(s3BucketName, s3ObjectKey);
			}
		}
		return s3FileInfo;
	}

	@Override
	public void store(String key, String filename) {
		s3FileUploader.store(key, filename);
	}

	class S3FileInfo {

		private final String bucketName;
		private final String objectKey;

		public S3FileInfo(String bucketName, String objectKey) {
			super();
			this.bucketName = bucketName;
			this.objectKey = objectKey;
		}

		public String getBucketName() {
			return bucketName;
		}

		public String getObjectKey() {
			return objectKey;
		}

	}
}
