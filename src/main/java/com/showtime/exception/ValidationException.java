package com.showtime.exception;

import org.springframework.http.HttpStatus;
import java.util.Map;

public class ValidationException extends CustomException {
    
    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
    
    public ValidationException(String message, Map<String, Object> details) {
        super(message, HttpStatus.BAD_REQUEST, details);
    }
}