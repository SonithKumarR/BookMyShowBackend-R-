package com.showtime.dto.response;



import lombok.Data;

@Data
public class PaymentResponse {
    private String paymentId;
    private String orderId;
    private Double amount;
    private String currency = "INR";
    private String status;
    private String razorpayKey;
    private String callbackUrl;
}
