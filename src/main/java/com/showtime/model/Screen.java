package com.showtime.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "screens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Screen {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;
    
    @Column(name = "screen_number")
    private Integer screenNumber;
    
    @Column(name = "screen_name")
    private String screenName;
    
    @Column(name = "total_seats")
    private Integer totalSeats = 100;
    
    @Column(name = "seats_layout")
    private String seatsLayout; // JSON array of seat layout
    
    @Column(name = "is_active")
    private boolean isActive = true;
    
    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<Show> shows = new java.util.ArrayList<>();
}