package com.showtime.exception;

import org.springframework.http.HttpStatus;
import java.util.Map;

public class PaymentException extends CustomException {
    
    public PaymentException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
    
    public PaymentException(String message, Map<String, Object> details) {
        super(message, HttpStatus.BAD_REQUEST, details);
    }
}