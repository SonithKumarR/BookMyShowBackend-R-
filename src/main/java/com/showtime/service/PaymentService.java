package com.showtime.service;

import com.showtime.dto.*;
import com.showtime.dto.response.*;
import com.showtime.dto.request.*;
import com.showtime.model.Booking;
import com.showtime.model.Payment;
import com.showtime.repository.BookingRepository;
import com.showtime.repository.PaymentRepository;
import com.showtime.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    
    @Value("${razorpay.key.id}")
    private String razorpayKeyId;
    
    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;
    
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final EmailService emailService;
    
    public PaymentResponse initiatePayment(PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        // In real app, integrate with Razorpay/Stripe
        // For now, generate mock payment response
        
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId("pay_" + UUID.randomUUID().toString().substring(0, 8));
        response.setOrderId("order_" + UUID.randomUUID().toString().substring(0, 8));
        response.setAmount(booking.getTotalAmount());
        response.setStatus("created");
        response.setRazorpayKey(razorpayKeyId);
        response.setCallbackUrl("/api/payments/verify");
        
        return response;
    }
    
    @Transactional
    public PaymentDTO verifyPayment(PaymentVerificationRequest request) {
        // In real app, verify Razorpay signature
        // boolean isValid = verifyRazorpaySignature(request);
        boolean isValid = true; // Mock verification
        
        if (!isValid) {
            throw new RuntimeException("Invalid payment signature");
        }
        
        // Find payment by order ID
        Payment payment = paymentRepository.findByGatewayTransactionId(request.getRazorpayOrderId())
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        // Update payment status
        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        payment.setGatewayTransactionId(request.getRazorpayPaymentId());
        payment.setTransactionId(request.getRazorpayPaymentId());
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);
        
        // Update booking payment status
        Booking booking = payment.getBooking();
        booking.setPaymentStatus(Booking.PaymentStatus.COMPLETED);
        bookingRepository.save(booking);
        
        // Send payment success email
        emailService.sendPaymentConfirmation(booking.getUser().getEmail(), booking, payment);
        
        return convertToDTO(payment);
    }
    
    @Transactional
    public PaymentDTO processRefund(Long paymentId, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        // In real app, process refund through payment gateway
        payment.setStatus(Payment.PaymentStatus.REFUNDED);
        payment.setRefundDate(LocalDateTime.now());
        payment.setRefundAmount(payment.getAmount());
        payment.setRefundReason(reason);
        paymentRepository.save(payment);
        
        // Update booking status
        Booking booking = payment.getBooking();
        booking.setPaymentStatus(Booking.PaymentStatus.REFUNDED);
        bookingRepository.save(booking);
        
        return convertToDTO(payment);
    }
    
    public PaymentDTO getPaymentDetails(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        return convertToDTO(payment);
    }
    
    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setPaymentGateway(payment.getPaymentGateway());
        dto.setTransactionId(payment.getTransactionId());
        dto.setAmount(payment.getAmount());
        dto.setStatus(payment.getStatus().name());
        if (payment.getBooking() != null) {
            dto.setBookingReference(payment.getBooking().getBookingReference());
        }
        return dto;
    }
}