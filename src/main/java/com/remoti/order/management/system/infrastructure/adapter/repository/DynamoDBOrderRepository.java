package com.remoti.order.management.system.infrastructure.adapter.repository;

import com.remoti.order.management.system.application.port.OrderRepository;
import com.remoti.order.management.system.domain.model.Order;
import com.remoti.order.management.system.enums.OrderStatus;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Map;

@Repository
public class DynamoDBOrderRepository implements OrderRepository {

    private final DynamoDbAsyncClient dynamoDbClient;

    public DynamoDBOrderRepository(DynamoDbAsyncClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    @Override
    public Mono<Order> save(Order order) {
        return Mono.fromFuture(() -> {
            PutItemRequest request = PutItemRequest.builder()
                    .tableName("Order")
                    .item(OrderMapper.toDynamoDbItem(order))
                    .build();
            return dynamoDbClient.putItem(request);
        }).thenReturn(order);
    }

    @Override
    public Mono<Order> findById(String orderId) {
        return Mono.fromFuture(() -> {
            GetItemRequest request = GetItemRequest.builder()
                    .tableName("Order")
                    .key(Map.of("orderId", AttributeValue.builder().s(orderId).build()))
                    .build();
            return dynamoDbClient.getItem(request);
        }).map(response -> OrderMapper.fromDynamoDbItem(response.item()));
    }

    @Override
    public Mono<Order> updateStatus(String orderId, OrderStatus status) {
        return Mono.fromFuture(() -> {
            UpdateItemRequest request = UpdateItemRequest.builder()
                    .tableName("Order")
                    .key(Map.of("orderId", AttributeValue.builder().s(orderId).build()))
                    .updateExpression("SET orderStatus = :status")
                    .expressionAttributeValues(Map.of(":status", AttributeValue.builder().s(status.name()).build()))
                    .build();
            return dynamoDbClient.updateItem(request);
        }).then(findById(orderId));
    }

    @Override
    public Flux<Order> findAll() {
        return Flux.from(Mono.fromFuture(() -> {
                    ScanRequest request = ScanRequest.builder()
                            .tableName("Order")
                            .build();

                    return dynamoDbClient.scan(request);
                })).flatMapIterable(ScanResponse::items)
                .map(OrderMapper::fromDynamoDbItem);
    }

    @Override
    public Mono<Void> deleteById(String orderId) {
        return Mono.fromFuture(() -> {
            DeleteItemRequest request = DeleteItemRequest.builder()
                    .tableName("Order")
                    .key(Map.of("orderId", AttributeValue.builder().s(orderId).build()))
                    .build();
            return dynamoDbClient.deleteItem(request);
        }).then();
    }

}