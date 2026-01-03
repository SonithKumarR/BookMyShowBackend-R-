package com.showtime.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "theaters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Theater {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private String address;
    
    @Column(nullable = false)
    private String city;
    
    private String state;
    
    private String pincode;
    
    private String phone;
    
    private String email;
    
    @Column(name = "total_screens")
    private Integer totalScreens = 1;
    
    @Column(name = "seats_per_screen")
    private Integer seatsPerScreen = 100;
    
    @Column(length = 1000)
    private String facilities;
    
    @Column(name = "is_active")
    private boolean isActive = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Show> shows = new ArrayList<>();
    
    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Screen> screens = new ArrayList<>();
}