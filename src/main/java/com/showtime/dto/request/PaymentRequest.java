package com.showtime.dto.request;


import lombok.Data;

@Data
public class PaymentRequest {
    private Long bookingId;
    private String paymentMethod;
    private String paymentGateway = "RAZORPAY";
}
