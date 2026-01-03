package com.showtime.service;

import com.showtime.model.Booking;
import com.showtime.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    @Async
    public void sendBookingConfirmation(String to, Booking booking) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject("Booking Confirmation - " + booking.getBookingReference());
            helper.setText(buildBookingConfirmationEmail(booking), true);
            
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Async
    public void sendPaymentConfirmation(String to, Booking booking, Payment payment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject("Payment Successful - " + booking.getBookingReference());
            helper.setText(buildPaymentConfirmationEmail(booking, payment), true);
            
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Async
    public void sendBookingCancellation(String to, Booking booking) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject("Booking Cancelled - " + booking.getBookingReference());
            helper.setText(buildBookingCancellationEmail(booking), true);
            
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Async
    public void sendWelcomeEmail(String to, String name) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject("Welcome to ShowTime!");
            helper.setText(buildWelcomeEmail(name), true);
            
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Async
    public void sendPasswordResetEmail(String to, String resetToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject("Password Reset Request - ShowTime");
            helper.setText(buildPasswordResetEmail(resetToken), true);
            
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Async
    public void sendPasswordResetConfirmation(String to) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject("Password Reset Successful - ShowTime");
            helper.setText(buildPasswordResetConfirmationEmail(), true);
            
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Async
    public void sendVerificationEmail(String to, String verificationToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject("Verify Your Email - ShowTime");
            helper.setText(buildVerificationEmail(verificationToken), true);
            
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // HTML Email Templates
    
    private String buildBookingConfirmationEmail(Booking booking) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #dc2626; color: white; padding: 20px; text-align: center; }
                    .content { background-color: #f9f9f9; padding: 30px; }
                    .booking-details { background-color: white; padding: 20px; border-radius: 5px; margin: 20px 0; }
                    .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Booking Confirmed!</h1>
                    </div>
                    <div class="content">
                        <h2>Thank you for your booking!</h2>
                        <div class="booking-details">
                            <h3>Booking Details</h3>
                            <p><strong>Booking Reference:</strong> %s</p>
                            <p><strong>Movie:</strong> %s</p>
                            <p><strong>Theater:</strong> %s</p>
                            <p><strong>Date & Time:</strong> %s at %s</p>
                            <p><strong>Seats:</strong> %s</p>
                            <p><strong>Total Amount:</strong> ₹%.2f</p>
                        </div>
                        <p>Please arrive at least 30 minutes before the showtime.</p>
                        <p>Show this email or the QR code at the theater entrance.</p>
                    </div>
                    <div class="footer">
                        <p>© 2024 ShowTime. All rights reserved.</p>
                        <p>If you have any questions, contact us at support@showtime.com</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(
                booking.getBookingReference(),
                booking.getShow().getMovie().getTitle(),
                booking.getShow().getTheater().getName(),
                booking.getShow().getShowDate(),
                booking.getShow().getShowTime(),
                booking.getSeatNumbers(),
                booking.getTotalAmount()
            );
    }
    
    private String buildPaymentConfirmationEmail(Booking booking, Payment payment) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #059669; color: white; padding: 20px; text-align: center; }
                    .content { background-color: #f9f9f9; padding: 30px; }
                    .payment-details { background-color: white; padding: 20px; border-radius: 5px; margin: 20px 0; }
                    .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Payment Successful!</h1>
                    </div>
                    <div class="content">
                        <h2>Your payment has been processed successfully</h2>
                        <div class="payment-details">
                            <h3>Payment Details</h3>
                            <p><strong>Transaction ID:</strong> %s</p>
                            <p><strong>Payment Method:</strong> %s</p>
                            <p><strong>Amount Paid:</strong> ₹%.2f</p>
                            <p><strong>Booking Reference:</strong> %s</p>
                            <p><strong>Date:</strong> %s</p>
                        </div>
                        <p>Your tickets have been confirmed. You can view your tickets in your account.</p>
                    </div>
                    <div class="footer">
                        <p>© 2024 ShowTime. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(
                payment.getTransactionId(),
                payment.getPaymentMethod(),
                payment.getAmount(),
                booking.getBookingReference(),
                payment.getPaymentDate()
            );
    }
    
    private String buildBookingCancellationEmail(Booking booking) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #dc2626; color: white; padding: 20px; text-align: center; }
                    .content { background-color: #f9f9f9; padding: 30px; }
                    .refund-details { background-color: white; padding: 20px; border-radius: 5px; margin: 20px 0; }
                    .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Booking Cancelled</h1>
                    </div>
                    <div class="content">
                        <h2>Your booking has been cancelled</h2>
                        <div class="refund-details">
                            <h3>Cancellation Details</h3>
                            <p><strong>Booking Reference:</strong> %s</p>
                            <p><strong>Movie:</strong> %s</p>
                            <p><strong>Amount Refunded:</strong> ₹%.2f</p>
                            <p><strong>Cancellation Date:</strong> %s</p>
                        </div>
                        <p>The refund amount will be credited to your original payment method within 5-7 business days.</p>
                    </div>
                    <div class="footer">
                        <p>© 2024 ShowTime. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(
                booking.getBookingReference(),
                booking.getShow().getMovie().getTitle(),
                booking.getTotalAmount(),
                booking.getCancelledAt()
            );
    }
    
    private String buildWelcomeEmail(String name) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #3b82f6; color: white; padding: 20px; text-align: center; }
                    .content { background-color: #f9f9f9; padding: 30px; }
                    .features { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin: 20px 0; }
                    .feature { background-color: white; padding: 15px; border-radius: 5px; text-align: center; }
                    .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Welcome to ShowTime!</h1>
                    </div>
                    <div class="content">
                        <h2>Hello %s,</h2>
                        <p>Thank you for joining ShowTime - your ultimate movie booking experience!</p>
                        
                        <div class="features">
                            <div class="feature">
                                <h3>🎬 Book Movies</h3>
                                <p>Browse and book tickets for latest movies</p>
                            </div>
                            <div class="feature">
                                <h3>📍 Find Theaters</h3>
                                <p>Discover theaters near you</p>
                            </div>
                            <div class="feature">
                                <h3>💺 Select Seats</h3>
                                <p>Choose your preferred seats</p>
                            </div>
                            <div class="feature">
                                <h3>💰 Easy Payments</h3>
                                <p>Secure and fast payment options</p>
                            </div>
                        </div>
                        
                        <p>Get ready to experience the magic of cinema!</p>
                        <p><a href="http://localhost:5173" style="background-color: #dc2626; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">Start Booking Now</a></p>
                    </div>
                    <div class="footer">
                        <p>© 2024 ShowTime. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(name);
    }
    
    private String buildPasswordResetEmail(String resetToken) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #f59e0b; color: white; padding: 20px; text-align: center; }
                    .content { background-color: #f9f9f9; padding: 30px; }
                    .reset-button { display: inline-block; background-color: #dc2626; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                    .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Password Reset Request</h1>
                    </div>
                    <div class="content">
                        <h2>Hello,</h2>
                        <p>We received a request to reset your password for your ShowTime account.</p>
                        <p>Click the button below to reset your password:</p>
                        
                        <a href="http://localhost:5173/reset-password?token=%s" class="reset-button">Reset Password</a>
                        
                        <p>This link will expire in 24 hours.</p>
                        <p>If you didn't request a password reset, please ignore this email.</p>
                    </div>
                    <div class="footer">
                        <p>© 2024 ShowTime. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(resetToken);
    }
    
    private String buildPasswordResetConfirmationEmail() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #059669; color: white; padding: 20px; text-align: center; }
                    .content { background-color: #f9f9f9; padding: 30px; }
                    .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Password Reset Successful</h1>
                    </div>
                    <div class="content">
                        <h2>Your password has been changed successfully!</h2>
                        <p>Your ShowTime account password has been reset.</p>
                        <p>If you didn't make this change, please contact our support team immediately at support@showtime.com.</p>
                        <p>For security reasons, we recommend that you:</p>
                        <ul>
                            <li>Use a strong, unique password</li>
                            <li>Enable two-factor authentication</li>
                            <li>Regularly update your password</li>
                        </ul>
                    </div>
                    <div class="footer">
                        <p>© 2024 ShowTime. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """;
    }
    
    private String buildVerificationEmail(String verificationToken) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #3b82f6; color: white; padding: 20px; text-align: center; }
                    .content { background-color: #f9f9f9; padding: 30px; }
                    .verify-button { display: inline-block; background-color: #dc2626; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                    .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Verify Your Email</h1>
                    </div>
                    <div class="content">
                        <h2>Welcome to ShowTime!</h2>
                        <p>Thank you for registering. Please verify your email address to activate your account.</p>
                        
                        <a href="http://localhost:5173/verify-email?token=%s" class="verify-button">Verify Email Address</a>
                        
                        <p>Or copy and paste this link in your browser:</p>
                        <p>http://localhost:5173/verify-email?token=%s</p>
                        
                        <p>This link will expire in 24 hours.</p>
                    </div>
                    <div class="footer">
                        <p>© 2024 ShowTime. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(verificationToken, verificationToken);
    }
}





































//package com.showtime.service;
//
//import com.showtime.model.Booking;
//import com.showtime.model.Payment;
//import lombok.RequiredArgsConstructor;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.context.Context; 
//
//import jakarta.mail.internet.MimeMessage;
//import java.time.format.DateTimeFormatter;
//
//@Service
//@RequiredArgsConstructor
//public class EmailService {
//  
//  private final JavaMailSender mailSender;
//  private final TemplateEngine templateEngine;
//  
//  @Async
//  public void sendBookingConfirmation(String to, Booking booking) {
//      try {
//          MimeMessage message = mailSender.createMimeMessage();
//          MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//          
//          helper.setTo(to);
//          helper.setSubject("Booking Confirmation - " + booking.getBookingReference());
//          
//          Context context = new Context();
//          context.setVariable("booking", booking);
//          context.setVariable("formatter", DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
//          
//          String htmlContent = templateEngine.process("booking-confirmation", context);
//          helper.setText(htmlContent, true);
//          
//          mailSender.send(message);
//      } catch (Exception e) {
//          e.printStackTrace();
//      }
//  }
//  
//  @Async
//  public void sendPaymentConfirmation(String to, Booking booking, Payment payment) {
//      try {
//          MimeMessage message = mailSender.createMimeMessage();
//          MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//          
//          helper.setTo(to);
//          helper.setSubject("Payment Successful - " + booking.getBookingReference());
//          
//          Context context = new Context();
//          context.setVariable("booking", booking);
//          context.setVariable("payment", payment);
//          context.setVariable("formatter", DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
//          
//          String htmlContent = templateEngine.process("payment-confirmation", context);
//          helper.setText(htmlContent, true);
//          
//          mailSender.send(message);
//      } catch (Exception e) {
//          e.printStackTrace();
//      }
//  }
//  
//  @Async
//  public void sendBookingCancellation(String to, Booking booking) {
//      try {
//          MimeMessage message = mailSender.createMimeMessage();
//          MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//          
//          helper.setTo(to);
//          helper.setSubject("Booking Cancelled - " + booking.getBookingReference());
//          
//          Context context = new Context();
//          context.setVariable("booking", booking);
//          context.setVariable("formatter", DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
//          
//          String htmlContent = templateEngine.process("booking-cancellation", context);
//          helper.setText(htmlContent, true);
//          
//          mailSender.send(message);
//      } catch (Exception e) {
//          e.printStackTrace();
//      }
//  }
//  
//  @Async
//  public void sendWelcomeEmail(String to, String name) {
//      try {
//          MimeMessage message = mailSender.createMimeMessage();
//          MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//          
//          helper.setTo(to);
//          helper.setSubject("Welcome to ShowTime!");
//          
//          Context context = new Context();
//          context.setVariable("name", name);
//          
//          String htmlContent = templateEngine.process("welcome-email", context);
//          helper.setText(htmlContent, true);
//          
//          mailSender.send(message);
//      } catch (Exception e) {
//          e.printStackTrace();
//      }
//  }
//  
//  // ADD THESE MISSING METHODS:
//  
//  @Async
//  public void sendPasswordResetEmail(String to, String resetToken) {
//      try {
//          MimeMessage message = mailSender.createMimeMessage();
//          MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//          
//          helper.setTo(to);
//          helper.setSubject("Password Reset Request - ShowTime");
//          
//          Context context = new Context();
//          context.setVariable("resetToken", resetToken);
//          context.setVariable("resetLink", "http://localhost:5173/reset-password?token=" + resetToken);
//          
//          String htmlContent = templateEngine.process("password-reset", context);
//          helper.setText(htmlContent, true);
//          
//          mailSender.send(message);
//      } catch (Exception e) {
//          e.printStackTrace();
//      }
//  }
//  
//  @Async
//  public void sendPasswordResetConfirmation(String to) {
//      try {
//          MimeMessage message = mailSender.createMimeMessage();
//          MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//          
//          helper.setTo(to);
//          helper.setSubject("Password Reset Successful - ShowTime");
//          
//          Context context = new Context();
//          context.setVariable("message", "Your password has been successfully reset.");
//          
//          String htmlContent = templateEngine.process("password-reset-confirmation", context);
//          helper.setText(htmlContent, true);
//          
//          mailSender.send(message);
//      } catch (Exception e) {
//          e.printStackTrace();
//      }
//  }
//  
//  @Async
//  public void sendVerificationEmail(String to, String verificationToken) {
//      try {
//          MimeMessage message = mailSender.createMimeMessage();
//          MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//          
//          helper.setTo(to);
//          helper.setSubject("Verify Your Email - ShowTime");
//          
//          Context context = new Context();
//          context.setVariable("verificationToken", verificationToken);
//          context.setVariable("verificationLink", 
//              "http://localhost:5173/verify-email?token=" + verificationToken);
//          
//          String htmlContent = templateEngine.process("email-verification", context);
//          helper.setText(htmlContent, true);
//          
//          mailSender.send(message);
//      } catch (Exception e) {
//          e.printStackTrace();
//      }
//  }
//  
//  @Async
//  public void sendBookingReminder(String to, Booking booking) {
//      try {
//          MimeMessage message = mailSender.createMimeMessage();
//          MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//          
//          helper.setTo(to);
//          helper.setSubject("Show Reminder - " + booking.getShow().getMovie().getTitle());
//          
//          Context context = new Context();
//          context.setVariable("booking", booking);
//          context.setVariable("formatter", DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
//          
//          String htmlContent = templateEngine.process("booking-reminder", context);
//          helper.setText(htmlContent, true);
//          
//          mailSender.send(message);
//      } catch (Exception e) {
//          e.printStackTrace();
//      }
//  }
//}