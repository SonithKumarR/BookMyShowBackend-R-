package com.showtime.dto;

import java.util.List;

import lombok.Data;

@Data
public class DashboardStatsDTO {
    private Long totalBookings;
    private Long totalMovies;
    private Long totalTheaters;
    private Long totalUsers;
    private Double totalRevenue;
    private List<RevenueChartDTO> revenueChart;
}
