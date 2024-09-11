package com.remoti.order.management.system.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Data
@AllArgsConstructor
@DynamoDbBean
public class Order {

    private String id;
    private String productName;
    private int quantity;
    private double price;
    private String status;

}