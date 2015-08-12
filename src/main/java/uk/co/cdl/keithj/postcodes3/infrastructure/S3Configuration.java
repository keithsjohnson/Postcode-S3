package uk.co.cdl.keithj.postcodes3.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.core.io.s3.SimpleStorageResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.transfer.TransferManager;

@Configuration
public class S3Configuration {

	@Autowired
	private Region region;

	@Bean
	public ResourceLoader s3ResourceLoader(AmazonS3 amazonS3) {
		amazonS3.setRegion(region);
		return new SimpleStorageResourceLoader(amazonS3);
	}

	@Bean
	public TransferManager s3TransferManager(AmazonS3 amazonS3) {
		amazonS3.setRegion(region);
		return new TransferManager(amazonS3);
	}
}
