package com.showtime.dto;

import lombok.Data;

@Data
public class SeatDTO {
    private String seatNumber;
    private String seatType;
    private Double price;
    private boolean isAvailable;
    private boolean isSelected;
}
