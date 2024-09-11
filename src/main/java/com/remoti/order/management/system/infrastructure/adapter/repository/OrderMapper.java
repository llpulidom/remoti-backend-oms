package com.remoti.order.management.system.infrastructure.adapter.repository;

import com.remoti.order.management.system.domain.model.Order;
import com.remoti.order.management.system.enums.OrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.math.BigDecimal;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    // De Order a Map<String, AttributeValue>
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

    // De Map<String, AttributeValue> a Order
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