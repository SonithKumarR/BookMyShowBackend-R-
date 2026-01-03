package com.showtime.dto.request;


import lombok.Data;

@Data
public class TheaterRequest {
    private String name;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String phone;
    private String email;
    private Integer totalScreens;
    private String facilities;
}
