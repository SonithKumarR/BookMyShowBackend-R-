package com.showtime.service;

import com.showtime.model.User;
import com.showtime.repository.UserRepository;
import com.showtime.service.EmailService;
import com.showtime.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import com.showtime.dto.response.*;
import com.showtime.dto.request.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;
    
    @Transactional
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        
        // Generate reset token
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        userRepository.save(user);
        
        // Send reset email
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
    }
    
    @Transactional
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        userRepository.save(user);
        
        // Send confirmation email
        emailService.sendPasswordResetConfirmation(user.getEmail());
    }
    
    @Transactional
    public void verifyEmail(String token) {
        User user = userRepository.findByEmailVerificationToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid verification token"));
        
        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        userRepository.save(user);
    }
    
    public void sendVerificationEmail(User user) {
        String token = UUID.randomUUID().toString();
        user.setEmailVerificationToken(token);
        userRepository.save(user);
        
        emailService.sendVerificationEmail(user.getEmail(), token);
    }
    
    public String generateToken(User user) {
        return jwtUtils.generateTokenFromUsername(user.getEmail());
    }
    
    public boolean validateToken(String token) {
        return jwtUtils.validateJwtToken(token);
    }
    
    public String getUsernameFromToken(String token) {
        return jwtUtils.getUserNameFromJwtToken(token);
    }
}