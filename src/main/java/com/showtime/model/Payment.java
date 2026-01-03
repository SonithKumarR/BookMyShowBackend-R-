package com.showtime.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    @Column(name = "payment_method")
    private String paymentMethod; // CARD, UPI, NETBANKING, WALLET
    
    @Column(name = "payment_gateway")
    private String paymentGateway; // RAZORPAY, STRIPE, PAYTM
    
    @Column(name = "transaction_id")
    private String transactionId;
    
    @Column(name = "gateway_transaction_id")
    private String gatewayTransactionId;
    
    @Column(name = "amount", nullable = false)
    private Double amount;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;
    
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    
    @Column(name = "refund_date")
    private LocalDateTime refundDate;
    
    @Column(name = "refund_amount")
    private Double refundAmount;
    
    @Column(name = "refund_reason")
    private String refundReason;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public enum PaymentStatus {
        PENDING, SUCCESS, FAILED, REFUNDED, CANCELLED
    }
}