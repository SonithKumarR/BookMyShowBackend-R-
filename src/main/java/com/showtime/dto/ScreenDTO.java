package com.showtime.dto;

import lombok.Data;

@Data
public class ScreenDTO {
    private Long id;
    private Integer screenNumber;
    private String screenName;
    private Integer totalSeats;
    private String seatsLayout;
    private boolean isActive;
}
