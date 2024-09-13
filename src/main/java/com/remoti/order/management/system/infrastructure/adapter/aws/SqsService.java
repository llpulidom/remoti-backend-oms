package com.remoti.order.management.system.infrastructure.adapter.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
public class SqsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqsService.class);
    private final SqsClient sqsClient;

    public SqsService(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public void sendOrderCreatedEvent(String orderId) {
        try {
            String messageBody = "Order created with ID: " + orderId;

            String queueUrl = "https://sqs.us-west-2.amazonaws.com/your-account-id/your-queue-name";
            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(messageBody)
                    .build();

            sqsClient.sendMessage(sendMsgRequest);
            LOGGER.info("Order creation event sent to SQS for the order {}", orderId);
        } catch (Exception e) {
            LOGGER.info("Error sending message to SQS: {}", e.getMessage());
        }
    }
}
