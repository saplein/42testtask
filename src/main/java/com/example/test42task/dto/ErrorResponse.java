package com.example.test42task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    @Schema(description = "Human readable message", example = "User not found with id: 1")
    private String message;
    @Schema(description = "Timestamp of response", example = "2023-10-10T12:00:00")
    private LocalDateTime timestamp;
    @Schema(description = "HTTP status", example = "404 NOT_FOUND")
    private String status;
    @Schema(description = "ID of user", example = "1")
    private Long userId;

    public ErrorResponse(String message, HttpStatus httpStatus, Long userId) {
        this.message = message;
        this.status = httpStatus.getReasonPhrase();
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
    }
}
