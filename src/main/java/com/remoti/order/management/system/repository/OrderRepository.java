package com.remoti.order.management.system.repository;

import com.remoti.order.management.system.model.Order;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;

@Repository
public class OrderRepository {

    private final DynamoDbAsyncTable<Order> orderTable;

    public OrderRepository(DynamoDbEnhancedAsyncClient client) {
        TableSchema<Order> orderTableSchema = TableSchema.fromBean(Order.class);
        this.orderTable = client.table("Orders", orderTableSchema);
    }

    public Mono<Order> save(Order order) {
        return Mono.fromFuture(() -> orderTable.putItem(PutItemEnhancedRequest.builder(Order.class)
                        .item(order)
                        .build()))
                .thenReturn(order);
    }

    public Mono<Order> findById(String id) {
        return Mono.fromFuture(() -> orderTable.getItem(GetItemEnhancedRequest.builder()
                .key(k -> k.partitionValue(id))
                .build()));
    }

    public Mono<Void> deleteById(String id) {
        return Mono.fromFuture(() -> orderTable.deleteItem(DeleteItemEnhancedRequest.builder()
                        .key(k -> k.partitionValue(id))
                        .build()))
                .then();
    }

}