package com.showtime.controller;

import com.showtime.dto.*;
import com.showtime.dto.request.*;
import com.showtime.dto.response.*;
import com.showtime.model.User;
import com.showtime.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getCurrentUser(user));
    }
    
    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateProfile(
            @RequestBody ProfileUpdateRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.updateProfile(user, request));
    }
    
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal User user) {
        userService.changePassword(user, request);
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }
    
    @GetMapping("/wishlist")
    public ResponseEntity<List<MovieDTO>> getWishlist(@AuthenticationPrincipal User user) {
        // Implementation for wishlist
        return ResponseEntity.ok(List.of());
    }
    
    @PostMapping("/wishlist/{movieId}")
    public ResponseEntity<?> addToWishlist(
            @PathVariable Long movieId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(Map.of("message", "Added to wishlist"));
    }
    
    @DeleteMapping("/wishlist/{movieId}")
    public ResponseEntity<?> removeFromWishlist(
            @PathVariable Long movieId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(Map.of("message", "Removed from wishlist"));
    }
}