package uk.co.keithj.postcodes3.infrastructure.sqs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.regions.Region;
import com.amazonaws.services.sqs.AmazonSQSAsync;

@Configuration
public class SQSConfiguration {

	@Autowired
	private Region region;

	@Bean
	public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSqs,
			ResourceIdResolver resourceIdResolver) {
		amazonSqs.setRegion(region);
		return new QueueMessagingTemplate(amazonSqs, resourceIdResolver);
	}
}
