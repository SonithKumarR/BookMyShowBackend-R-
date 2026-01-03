package com.showtime.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shows")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Show {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;
    
    @Column(name = "show_date", nullable = false)
    private LocalDate showDate;
    
    @Column(name = "show_time", nullable = false)
    private LocalTime showTime;
    
    @Column(name = "end_time")
    private LocalTime endTime;
    
    @Column(name = "price_classic")
    private Double priceClassic = 200.0;
    
    @Column(name = "price_premium")
    private Double pricePremium = 350.0;
    
    @Column(name = "available_seats")
    private Integer availableSeats;
    
    @Column(name = "booked_seats")
    private String bookedSeats = "[]"; // JSON array of booked seats
    
    @Column(name = "is_active")
    private boolean isActive = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings = new ArrayList<>();
    
    @PrePersist
    public void prePersist() {
        if (availableSeats == null && screen != null) {
            availableSeats = screen.getTotalSeats();
        }
        if (endTime == null && movie != null && showTime != null) {
            endTime = showTime.plusMinutes(movie.getDuration());
        }
    }
}