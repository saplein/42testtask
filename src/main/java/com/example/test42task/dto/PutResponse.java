package com.example.test42task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PutResponse {
    @Schema(description = "Human readable message", example = "User updated successfully")
    private String message;

    @Schema(description = "Timestamp of response", example = "2023-10-10T12:00:00")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status", example = "200 OK")
    private String status;

    @Schema(description = "ID of updated user", example = "1")
    private Long userId;

    public PutResponse(String message, Long userId) {
        this.message = message;
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
        this.status = "200 OK";
    }
}
