package com.remoti.order.management.system.infrastructure.adapter.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.List;

@Service
public class SqsConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqsConsumerService.class);
    private final SqsClient sqsClient;
    private final String queueUrl = "https://sqs.us-west-2.amazonaws.com/your-account-id/your-queue-name";

    public SqsConsumerService(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    @Scheduled(fixedRate = 10000)
    public void pollSqsForMessages() {
        try {
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(10)
                    .build();

            List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();

            for (Message message : messages) {
                LOGGER.info("Message received from SQS: {}", message.body());

                // change status

                sqsClient.deleteMessage(builder -> builder.queueUrl(queueUrl).receiptHandle(message.receiptHandle()));
            }

        } catch (Exception e) {
            LOGGER.info("Error processing SQS messages: {}", e.getMessage());
        }
    }
}
