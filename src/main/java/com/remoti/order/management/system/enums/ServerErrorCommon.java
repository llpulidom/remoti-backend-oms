package com.remoti.order.management.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ServerErrorCommon {
    INTERNAL_ERROR("X0001", "Unknown Error.", HttpStatus.INTERNAL_SERVER_ERROR),
    REQUEST_FAILED("X0002", "Error in the request or some field may be empty.", HttpStatus.BAD_REQUEST),
    FALLBACK("X0003", "Please try again, we are experiencing some issues.", HttpStatus.SERVICE_UNAVAILABLE),
    CIRCUIT_BREAKER("X0004", "Error, services are currently unavailable, please try again.", HttpStatus.SERVICE_UNAVAILABLE);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}