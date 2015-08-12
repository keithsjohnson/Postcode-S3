package uk.co.cdl.keithj.postcodes3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

@SpringBootApplication
public class PostcodeS3MainApplication {

	@Value("${use.profile.credentials:false}")
	private boolean useProfileCredentials = false;

	@Value("${amazon.region:eu-west-1}")
	private String amazonRegion;

	public static void main(String[] args) {
		SpringApplication.run(PostcodeS3MainApplication.class, args);
	}

	@Bean
	public AWSCredentials amazonAWSCredentials() {
		if (useProfileCredentials) {
			return new ProfileCredentialsProvider().getCredentials();
		} else {
			return new InstanceProfileCredentialsProvider().getCredentials();
		}
	}

	@Bean
	public Region amazonRegion() {
		return Region.getRegion(Regions.fromName(amazonRegion));
	}
}
