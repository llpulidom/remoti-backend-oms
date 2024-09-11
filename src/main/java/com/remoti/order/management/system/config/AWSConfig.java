package com.remoti.order.management.system.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@Configuration
public class AWSConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AWSConfig.class);

    @Bean
    public DynamoDbAsyncClient dynamoDbAsyncClient() {
        LOGGER.info("Starting DynamoDB client");
        return DynamoDbAsyncClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }

    @Bean
    public DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient(DynamoDbAsyncClient dynamoDbAsyncClient) {
        LOGGER.info("Starting DynamoDB Async client");
        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(dynamoDbAsyncClient)
                .build();
    }

}
