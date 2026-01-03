package com.showtime.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;



import lombok.Data;

@Data
public class ShowRequest {
    private Long movieId;
    private Long theaterId;
    private Long screenId;
    private LocalDate showDate;
    private LocalTime showTime;
    private Double priceClassic;
    private Double pricePremium;
}
