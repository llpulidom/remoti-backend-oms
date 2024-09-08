package com.remoti.order.management.system.model;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Data
@DynamoDbBean
public class Order {

    private String id;
    private String productName;
    private int quantity;
    private double price;
    private String status;

}