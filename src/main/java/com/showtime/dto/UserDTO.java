package com.showtime.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String role;
    private boolean emailVerified;
    private LocalDateTime createdAt;
    private List<BookingDTO> bookings;
    private List<MovieDTO> wishlist;
}



