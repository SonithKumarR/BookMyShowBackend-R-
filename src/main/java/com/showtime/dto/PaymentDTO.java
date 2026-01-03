package com.showtime.dto;

import lombok.Data;

@Data
public class PaymentDTO {
    private Long id;
    private String paymentMethod;
    private String paymentGateway;
    private String transactionId;
    private Double amount;
    private String status;
    private String bookingReference;
}


