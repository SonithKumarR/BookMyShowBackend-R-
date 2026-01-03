package com.showtime.dto;

import lombok.Data;
import java.util.List;

@Data
public class TheaterDTO {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String phone;
    private String email;
    private Integer totalScreens;
    private String facilities;
    private boolean isActive;
    private List<ShowDTO> shows;
}


