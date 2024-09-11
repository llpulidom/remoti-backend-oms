package com.remoti.order.management.system.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentDetails {

    private String cardNumber;
    private String cardHolder;
    private String expirationDate;
    private String cvv;

}