package com.showtime.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class MovieRequest {
    private String title;
    private String description;
    private String cast;
    private String director;
    private Integer duration;
    private String language;
    private String genre;
    private LocalDate releaseDate;
    private String posterUrl;
    private String trailerUrl;
    private String bannerUrl;
    private String certification;
}
