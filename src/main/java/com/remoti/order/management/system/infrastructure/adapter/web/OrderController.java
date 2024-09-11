package com.remoti.order.management.system.infrastructure.adapter.web;

import com.remoti.order.management.system.domain.model.Order;
import com.remoti.order.management.system.domain.model.PaymentDetails;
import com.remoti.order.management.system.domain.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Mono<ResponseEntity<Order>> createOrder(@RequestBody Order order) {
        return orderService.createOrder(order)
                .map(savedOrder -> ResponseEntity.status(HttpStatus.CREATED).body(savedOrder));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Order>> getOrder(@PathVariable String id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/pay")
    public Mono<ResponseEntity<Order>> payOrder(@PathVariable String id, @RequestBody PaymentDetails paymentDetails) {
        return orderService.processPayment(id, paymentDetails)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<Order> updateOrder(@PathVariable String id, @RequestBody Order order) {
        return orderService.updateOrder(id, order);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteOrder(@PathVariable String id) {
        return orderService.deleteOrder(id);
    }

}