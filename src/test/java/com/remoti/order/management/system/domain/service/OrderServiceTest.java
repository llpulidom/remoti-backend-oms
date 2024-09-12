package com.remoti.order.management.system.domain.service;

import com.remoti.order.management.system.domain.model.Order;
import com.remoti.order.management.system.application.port.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindOrderById() {
        String orderId = "123";
        Order mockOrder = new Order(orderId, "ProductA", 1, 100.0, "PENDING");
        when(orderRepository.findById(orderId)).thenReturn(Mono.just(mockOrder));

        Order foundOrder = orderService.getOrderById(orderId).block();

        assertNotNull(foundOrder);
        assertEquals(orderId, foundOrder.getId());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testCreateOrder() {
        Order newOrder = new Order("124", "ProductB", 2, 200.0, "NEW");
        when(orderRepository.save(newOrder)).thenReturn(Mono.just(newOrder));

        Order createdOrder = orderService.createOrder(newOrder).block();

        assertNotNull(createdOrder);
        assertEquals("ProductB", createdOrder.getProductName());
        verify(orderRepository, times(1)).save(newOrder);
    }

}
