package com.showtime.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TicketDTO {
    private Long id;
    private String ticketNumber;
    private String seatNumber;
    private String seatType;
    private Double price;
    private String qrCodeUrl;
    private Boolean isUsed;
    private LocalDateTime usedAt;
    private LocalDateTime createdAt;
}