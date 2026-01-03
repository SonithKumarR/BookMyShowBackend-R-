package com.showtime.dto;

import java.util.List;

import lombok.Data;

@Data
public class BookingHistoryDTO {
    private List<BookingDTO> upcomingBookings;
    private List<BookingDTO> pastBookings;
    private List<BookingDTO> cancelledBookings;
}
