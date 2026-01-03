package com.showtime.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class ApiResponse {
    private Boolean success;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    private Map<String, Object> data;
    
    // Constructor 1: success, message
    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
    
    // Constructor 2: success, message, path
    public ApiResponse(Boolean success, String message, String path) {
        this.success = success;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }
    
    // Constructor 3: success, message, timestamp, path
    public ApiResponse(Boolean success, String message, LocalDateTime timestamp, String path) {
        this.success = success;
        this.message = message;
        this.timestamp = timestamp;
        this.path = path;
    }
    
    // Constructor 4: success, message, data (Map<String, Object>)
    public ApiResponse(Boolean success, String message, Map<String, Object> data) {
        this.success = success;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.data = data;
    }
    
    // Constructor 5: success, message, timestamp, path, data
    public ApiResponse(Boolean success, String message, LocalDateTime timestamp, 
                      String path, Map<String, Object> data) {
        this.success = success;
        this.message = message;
        this.timestamp = timestamp;
        this.path = path;
        this.data = data;
    }
    
    // Static factory methods
    public static ApiResponse success(String message) {
        return new ApiResponse(true, message);
    }
    
    public static ApiResponse success(String message, Map<String, Object> data) {
        return new ApiResponse(true, message, data);
    }
    
    public static ApiResponse success(String message, String path) {
        return new ApiResponse(true, message, path);
    }
    
    public static ApiResponse error(String message) {
        return new ApiResponse(false, message);
    }
    
    public static ApiResponse error(String message, String path) {
        return new ApiResponse(false, message, path);
    }
    
    public static ApiResponse error(String message, Map<String, Object> data) {
        return new ApiResponse(false, message, data);
    }
    
    public static ApiResponse error(String message, LocalDateTime timestamp, String path) {
        return new ApiResponse(false, message, timestamp, path);
    }
}