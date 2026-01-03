package com.showtime.dto.request;

import java.time.LocalDate;



import lombok.Data;

@Data
public class MovieFilterRequest {
    private String city;
    private String language;
    private String genre;
    private LocalDate date;
    private String sortBy = "releaseDate";
    private String sortOrder = "desc";
}
