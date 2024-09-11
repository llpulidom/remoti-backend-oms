package com.remoti.order.management.system.application.port;

import com.remoti.order.management.system.domain.model.Order;
import com.remoti.order.management.system.enums.OrderStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderRepository {

    // Guarda un pedido
    Mono<Order> save(Order order);

    // Obtiene un pedido por su ID
    Mono<Order> findById(String orderId);

    // Actualiza el estado de un pedido
    Mono<Order> updateStatus(String orderId, OrderStatus status);

    // Obtiene todos los pedidos
    Flux<Order> findAll();

    // Elimina un pedido por su ID
    Mono<Void> deleteById(String orderId);

}