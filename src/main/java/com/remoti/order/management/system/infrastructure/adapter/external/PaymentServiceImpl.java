package com.remoti.order.management.system.infrastructure.adapter.external;

import com.remoti.order.management.system.application.port.PaymentService;
import com.remoti.order.management.system.domain.model.Order;
import com.remoti.order.management.system.domain.model.PaymentDetails;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final WebClient webClient;

    public PaymentServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://payment-service.com").build();
    }

    @Override
    @Retry(name = "paymentService")
    @CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
    public Mono<Void> processPayment(Order order, PaymentDetails paymentDetails) {
        return webClient.post()
                .uri("/processPayment")
                .bodyValue(paymentDetails)
                .retrieve()
                .bodyToMono(Void.class);
    }

    private CompletableFuture<Void> paymentFallback(Order order, PaymentDetails paymentDetails, Throwable t) {
        // Fallback logic, e.g., mark order as FAILED
        return CompletableFuture.completedFuture(null);
    }
}
