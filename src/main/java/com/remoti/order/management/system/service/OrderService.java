package com.remoti.order.management.system.service;

import com.remoti.order.management.system.model.Order;
import com.remoti.order.management.system.repository.OrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Mono<Order> createOrder(Order order) {
        order.setStatus("CREATED");
        return orderRepository.save(order);
    }

    public Mono<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    public Mono<Order> updateOrder(String id, Order updatedOrder) {
        return orderRepository.findById(id)
                .flatMap(existingOrder -> {
                    existingOrder.setProductName(updatedOrder.getProductName());
                    existingOrder.setQuantity(updatedOrder.getQuantity());
                    existingOrder.setPrice(updatedOrder.getPrice());
                    existingOrder.setStatus(updatedOrder.getStatus());
                    return orderRepository.save(existingOrder);
                });
    }

    public Mono<Void> deleteOrder(String id) {
        return orderRepository.deleteById(id);
    }

}