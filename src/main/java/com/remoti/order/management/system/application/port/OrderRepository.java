package com.remoti.order.management.system.application.port;

import com.remoti.order.management.system.domain.model.Order;
import com.remoti.order.management.system.enums.OrderStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderRepository {

    Mono<Order> save(Order order);

    Mono<Order> findById(String orderId);

    Mono<Order> updateStatus(String orderId, OrderStatus status);

    Flux<Order> findAll();

    Mono<Void> deleteById(String orderId);

}