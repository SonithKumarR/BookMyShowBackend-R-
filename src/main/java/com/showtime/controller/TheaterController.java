package com.showtime.controller;

import com.showtime.dto.*;
import com.showtime.dto.request.*;
import com.showtime.dto.response.*;
import com.showtime.service.TheaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/theaters")
@RequiredArgsConstructor
public class TheaterController {
    
    private final TheaterService theaterService;
    
    @GetMapping
    public ResponseEntity<List<TheaterDTO>> getAllTheaters() {
        return ResponseEntity.ok(theaterService.getAllTheaters());
    }
    
    @GetMapping("/cities")
    public ResponseEntity<List<String>> getAllCities() {
        return ResponseEntity.ok(theaterService.getAllCities());
    }
    
    @GetMapping("/city/{city}")
    public ResponseEntity<List<TheaterDTO>> getTheatersByCity(@PathVariable String city) {
        return ResponseEntity.ok(theaterService.getTheatersByCity(city));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TheaterDTO> getTheaterById(@PathVariable Long id) {
        return ResponseEntity.ok(theaterService.getTheaterById(id));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TheaterDTO> createTheater(@RequestBody TheaterRequest request) {
        return ResponseEntity.ok(theaterService.createTheater(request));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TheaterDTO> updateTheater(@PathVariable Long id, @RequestBody TheaterRequest request) {
        return ResponseEntity.ok(theaterService.updateTheater(id, request));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTheater(@PathVariable Long id) {
        theaterService.deleteTheater(id);
        return ResponseEntity.ok(Map.of("message", "Theater deleted successfully"));
    }
}