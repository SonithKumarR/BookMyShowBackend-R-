package com.showtime.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import com.showtime.dto.response.*;
import com.showtime.dto.request.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.UUID;

@Service
public class UPIPaymentService {
    
    @Value("${phonepe.merchant.id}")
    private String merchantId;
    
    @Value("${phonepe.salt.key}")
    private String saltKey;
    
    @Value("${phonepe.salt.index}")
    private String saltIndex;
    
    @Value("${app.frontend.url}")
    private String frontendUrl;
    
    public JSONObject initiateUPIPayment(String amount, String orderId, String customerPhone) {
        try {
            JSONObject request = new JSONObject();
            request.put("merchantId", merchantId);
            request.put("merchantTransactionId", orderId);
            request.put("merchantUserId", "MUID" + UUID.randomUUID().toString().substring(0, 8));
            request.put("amount", Integer.parseInt(amount) * 100);
            request.put("redirectUrl", frontendUrl + "/payment-success");
            request.put("redirectMode", "POST");
            request.put("callbackUrl", frontendUrl + "/api/payments/upi/callback");
            request.put("mobileNumber", customerPhone);
            request.put("paymentInstrument", new JSONObject().put("type", "UPI"));
            
            String base64EncodedPayload = Base64.getEncoder()
                .encodeToString(request.toString().getBytes());
            String checksum = calculateSHA256(base64EncodedPayload + 
                "/pg/v1/pay" + saltKey) + "###" + saltIndex;
            
            JSONObject response = new JSONObject();
            response.put("request", base64EncodedPayload);
            response.put("checksum", checksum);
            response.put("url", "https://api-preprod.phonepe.com/apis/pg-sandbox/pg/v1/pay");
            
            return response;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to initiate UPI payment: " + e.getMessage());
        }
    }
    
    private String calculateSHA256(String data) {
        try {
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(saltKey.getBytes(), "HmacSHA256");
            sha256HMAC.init(secretKey);
            byte[] hash = sha256HMAC.doFinal(data.getBytes());
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error calculating SHA256");
        }
    }
}