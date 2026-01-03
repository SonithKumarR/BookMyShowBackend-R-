package com.showtime.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    @Column(name = "ticket_number", unique = true)
    private String ticketNumber;
    
    @Column(name = "seat_number")
    private String seatNumber;
    
    @Column(name = "seat_type")
    private String seatType;
    
    @Column(name = "price")
    private Double price;
    
    @Column(name = "qr_code_url")
    private String qrCodeUrl;
    
    @Column(name = "is_used")
    private boolean isUsed = false;
    
    @Column(name = "used_at")
    private LocalDateTime usedAt;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}