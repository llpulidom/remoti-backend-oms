package com.remoti.order.management.system.infrastructure.adapter.repository;

import com.remoti.order.management.system.domain.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderId", source = "orderId")
    static Map<String, AttributeValue> toDynamoDbItem(Order order) {
        return Map.of(
                "orderId", AttributeValue.builder().s(order.getId()).build(),
                "productName", AttributeValue.builder().s(order.getProductName()).build(),
                "quantity", AttributeValue.builder().n(String.valueOf(order.getQuantity())).build(),
                "price", AttributeValue.builder().n(String.valueOf(order.getPrice())).build(),
                "status", AttributeValue.builder().s(order.getStatus()).build()
        );
    }

    static Order fromDynamoDbItem(Map<String, AttributeValue> item) {
        if (item == null || item.isEmpty()) {
            return null;
        }

        return new Order(
                item.get("orderId").s(),
                item.get("productName").s(),
                Integer.parseInt(item.get("quantity").n()),
                Double.parseDouble(item.get("price").n()),
                item.get("status").s()
        );
    }
}