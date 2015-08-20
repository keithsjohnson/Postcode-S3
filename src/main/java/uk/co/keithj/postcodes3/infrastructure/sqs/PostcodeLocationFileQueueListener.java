package uk.co.keithj.postcodes3.infrastructure.sqs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Component;

import uk.co.keithj.postcodes3.infrastructure.s3.S3ProviderImpl;

@Component
public class PostcodeLocationFileQueueListener {

	private static final String POSTCODELOCATIONFILEQUEUE = "PostcodeLocationFileQueue";

	@Autowired
	private S3ProviderImpl s3ProviderImpl;

	@MessageMapping(POSTCODELOCATIONFILEQUEUE)
	public void postcodeLocationFinderQueueListener(String message) {
		System.out.println("SQS " + POSTCODELOCATIONFILEQUEUE + " message received processing: " + message);
		s3ProviderImpl.retrieve(message);
		System.out.println("SQS " + POSTCODELOCATIONFILEQUEUE + " message received processed: " + message);
	}

}
