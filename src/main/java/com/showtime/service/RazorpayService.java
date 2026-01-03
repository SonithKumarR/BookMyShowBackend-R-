package com.showtime.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.showtime.dto.response.PaymentResponse;
import com.showtime.model.Booking;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class RazorpayService {
    
    private final RazorpayClient razorpayClient;
    
    @Value("${razorpay.key.id}")
    private String razorpayKeyId;
    
    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;
    
    @Value("${app.frontend.url}")
    private String frontendUrl;
    
    public PaymentResponse createOrder(Booking booking) throws RazorpayException {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int) (booking.getTotalAmount() * 100));
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", booking.getBookingReference());
        orderRequest.put("payment_capture", 1);
        
        JSONObject notes = new JSONObject();
        notes.put("booking_id", booking.getId().toString());
        notes.put("movie", booking.getShow().getMovie().getTitle());
        notes.put("theater", booking.getShow().getTheater().getName());
        notes.put("user_email", booking.getUser().getEmail());
        orderRequest.put("notes", notes);
        
        Order order = razorpayClient.orders.create(orderRequest);
        
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId("pay_" + UUID.randomUUID().toString().substring(0, 8));
        response.setOrderId(order.get("id"));
        response.setAmount(booking.getTotalAmount());
        response.setCurrency("INR");
        response.setStatus("created");
        response.setRazorpayKey(razorpayKeyId);
        response.setCallbackUrl(frontendUrl + "/payment-success");
        
        return response;
    }
    
    public boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, 
                                 String razorpaySignature) {
        try {
            JSONObject verificationData = new JSONObject();
            verificationData.put("razorpay_order_id", razorpayOrderId);
            verificationData.put("razorpay_payment_id", razorpayPaymentId);
            verificationData.put("razorpay_signature", razorpaySignature);
            
            com.razorpay.Utils.verifyPaymentSignature(verificationData, razorpayKeySecret);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}