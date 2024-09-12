package com.remoti.order.management.system.infrastructure.adapter.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remoti.order.management.system.domain.model.Order;
import com.remoti.order.management.system.domain.service.OrderService;import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrderById() throws Exception {
        String orderId = "123";
        Order mockOrder = new Order(orderId, "ProductA", 1, 100.0, "PENDING");
        when(orderService.getOrderById(orderId)).thenReturn(Mono.just(mockOrder));

        mockMvc.perform(get("/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.product").value("ProductA"))
                .andDo(print());

        verify(orderService, times(1)).getOrderById(orderId);
    }

    @Test
    void testCreateOrder() throws Exception {
        Order newOrder = new Order("124", "ProductB", 2, 200.0, "NEW");
        when(orderService.createOrder(any(Order.class))).thenReturn(Mono.just(newOrder));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"124\",\"product\":\"ProductB\",\"quantity\":2,\"price\":200.0,\"status\":\"NEW\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("124"))
                .andExpect(jsonPath("$.product").value("ProductB"))
                .andDo(print());

        verify(orderService, times(1)).createOrder(any(Order.class));
    }

    @Test
    void testUpdateOrderStatus() throws Exception {
        String orderId = "123";
        Order newOrder = new Order("123", "ProductB", 2, 200.0, "COMPLETED");
        when(orderService.updateOrder(orderId, newOrder)).thenReturn(Mono.just(newOrder));

        mockMvc.perform(put("/orders/{id}/status", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"COMPLETED\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order status updated successfully"))
                .andDo(print());

        verify(orderService, times(1)).updateOrder(orderId, newOrder);
    }
}

