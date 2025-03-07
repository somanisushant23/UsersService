package com.userservice.dtos;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse extends ApiResponse {
    private LocalDateTime timestamp;

    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
