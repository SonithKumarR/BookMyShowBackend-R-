package com.showtime.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class MovieDTO {
    private Long id;
    private String title;
    private String description;
    private String cast;
    private String director;
    private Integer duration;
    private String language;
    private String genre;
    private Double rating;
    private LocalDate releaseDate;
    private String posterUrl;
    private String trailerUrl;
    private String bannerUrl;
    private String certification;
    private boolean isActive;
    private Long totalShows;
    private List<ShowDTO> upcomingShows;
}
