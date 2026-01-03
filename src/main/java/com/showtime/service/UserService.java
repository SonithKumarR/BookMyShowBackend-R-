package com.showtime.service;

import com.showtime.dto.*;
import com.showtime.dto.response.*;
import com.showtime.dto.request.*;
import com.showtime.model.User;
import com.showtime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserDTO getCurrentUser(User user) {
        return convertToDTO(user);
    }
    
    @Transactional
    public UserDTO updateProfile(User user, ProfileUpdateRequest request) {
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user = userRepository.save(user);
        return convertToDTO(user);
    }
    
    @Transactional
    public void changePassword(User user, ChangePasswordRequest request) {
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
    
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public UserDTO updateUserRole(Long userId, String role) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(User.Role.valueOf(role));
        user = userRepository.save(user);
        return convertToDTO(user);
    }
    
    public Long getTotalUsers() {
        return userRepository.countAllUsers();
    }
    
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole().name());
        dto.setEmailVerified(user.isEmailVerified());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}