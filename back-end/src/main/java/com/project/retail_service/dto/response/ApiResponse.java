package com.project.retail_service.dto.response;

import java.time.LocalDateTime;

public record ApiResponse<T>(String status, String message, LocalDateTime timestamp, T data, String error) {
    public ApiResponse(String status, String message, T data) {
        this(status, message, LocalDateTime.now(), data, null);
    }

    public ApiResponse(String status, String message) {
        this(status, message, LocalDateTime.now(), null, null);
    }

    public ApiResponse(String status, String message, String error) {
        this(status, message, LocalDateTime.now(), null, error);
    }
}
