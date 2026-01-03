package com.showtime.exception;

import org.springframework.http.HttpStatus;
import java.util.Map;

public class CustomException extends RuntimeException {
    
    private final HttpStatus status;
    private final Map<String, Object> details;
    
    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.details = null;
    }
    
    public CustomException(String message, HttpStatus status, Map<String, Object> details) {
        super(message);
        this.status = status;
        this.details = details;
    }
    
    public HttpStatus getStatus() {
        return status;
    }
    
    public Map<String, Object> getDetails() {
        return details;
    }
}