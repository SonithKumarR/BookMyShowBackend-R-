package com.showtime.controller;

import com.showtime.dto.*;
import com.showtime.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    
    private final UserService userService;
    private final MovieService movieService;
    private final TheaterService theaterService;
    private final ShowService showService;
    private final BookingService bookingService;
    
    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setTotalBookings(bookingService.getTotalBookings());
        stats.setTotalMovies(movieService.getTotalMovies());
        stats.setTotalTheaters(theaterService.getTotalTheaters());
        stats.setTotalUsers(userService.getTotalUsers());
        stats.setTotalRevenue(bookingService.getTotalRevenue());
        
        // Get monthly revenue for last 6 months
        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);
        List<Object[]> revenueData = List.of(); // bookingService.getMonthlyRevenue(sixMonthsAgo);
        
        List<RevenueChartDTO> revenueChart = revenueData.stream()
            .map(data -> {
                RevenueChartDTO chart = new RevenueChartDTO();
                chart.setMonth((String) data[0]);
                chart.setRevenue((Double) data[1]);
                return chart;
            })
            .toList();
        
        stats.setRevenueChart(revenueChart);
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @PutMapping("/users/{id}/role")
    public ResponseEntity<UserDTO> updateUserRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        return ResponseEntity.ok(userService.updateUserRole(id, request.get("role")));
    }
    
    @GetMapping("/reports/bookings")
    public ResponseEntity<List<BookingDTO>> getBookingReports(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        // Implementation for booking reports
        return ResponseEntity.ok(List.of());
    }
    
    @GetMapping("/reports/revenue")
    public ResponseEntity<Map<String, Object>> getRevenueReports(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        // Implementation for revenue reports
        return ResponseEntity.ok(Map.of());
    }
}