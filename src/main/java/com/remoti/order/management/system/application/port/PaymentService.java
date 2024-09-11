package com.remoti.order.management.system.application.port;

import com.remoti.order.management.system.domain.model.Order;
import com.remoti.order.management.system.domain.model.PaymentDetails;
import reactor.core.publisher.Mono;

public interface PaymentService {

    Mono<Void> processPayment(Order order, PaymentDetails paymentDetails);

}