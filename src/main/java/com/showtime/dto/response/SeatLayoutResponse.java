package com.showtime.dto.response;

import java.util.List;

import com.showtime.dto.SeatDTO;

import lombok.Data;

@Data
public class SeatLayoutResponse {
    private List<List<SeatDTO>> seatLayout;
    private Double classicPrice;
    private Double premiumPrice;
    private List<String> bookedSeats;
}
