package com.showtime.dto.request;

import java.time.LocalDate;


import lombok.Data;

@Data
public class MovieSearchRequest {
    private String query;
    private String city;
    private LocalDate date;
}
