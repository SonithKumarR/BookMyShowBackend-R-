package com.showtime.dto.request;



import lombok.Data;

@Data
public class PaymentVerificationRequest {
    private String razorpayPaymentId;
    private String razorpayOrderId;
    private String razorpaySignature;
}
