package com.remoti.order.management.system.domain.service;

import com.remoti.order.management.system.domain.model.Order;
import com.remoti.order.management.system.application.port.OrderRepository;
import com.remoti.order.management.system.application.port.PaymentService;
import com.remoti.order.management.system.domain.model.PaymentDetails;
import com.remoti.order.management.system.enums.OrderStatus;
import com.remoti.order.management.system.infrastructure.adapter.aws.S3Service;
import com.remoti.order.management.system.infrastructure.adapter.aws.SqsService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final S3Service s3Service;
    private final SqsService sqsService;

    public OrderService(OrderRepository orderRepository, PaymentService paymentService, S3Service s3Service, SqsService sqsService) {
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
        this.s3Service = s3Service;
        this.sqsService = sqsService;
    }

    public Mono<Order> createOrder(Order order) {
        order.setStatus(OrderStatus.CREATED.name());

        String filePath = "src/main/resources/receipt.pdf";
        s3Service.uploadOrderReceipt(order.getId(), filePath);

        sqsService.sendOrderCreatedEvent(order.getId());

        return orderRepository.save(order);
    }

    public Mono<Order> processPayment(String orderId, PaymentDetails paymentDetails) {
        return orderRepository.findById(orderId)
                .flatMap(order -> paymentService.processPayment(order, paymentDetails)
                        .then(orderRepository.updateStatus(orderId, OrderStatus.PAID)));
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