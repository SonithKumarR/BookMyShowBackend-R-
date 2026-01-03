package com.showtime.dto.request;



import lombok.Data;

@Data
public class ScreenRequest {
    private Integer screenNumber;
    private String screenName;
    private Integer totalSeats;
    private String seatsLayout;
}
