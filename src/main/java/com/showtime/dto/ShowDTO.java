package com.showtime.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class ShowDTO {
    private Long id;
    private MovieDTO movie;
    private TheaterDTO theater;
    private ScreenDTO screen;
    private LocalDate showDate;
    private LocalTime showTime;
    private LocalTime endTime;
    private Double priceClassic;
    private Double pricePremium;
    private Integer availableSeats;
    private String bookedSeats;
    private boolean isActive;
}
