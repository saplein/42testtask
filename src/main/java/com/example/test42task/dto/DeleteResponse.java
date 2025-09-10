package com.example.test42task.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeleteResponse {
    private String message;
    private LocalDateTime timestamp;
    private String status;
    private Long deletedUserId;

    public DeleteResponse() {}

    public DeleteResponse(String message, Long deletedUserId) {
        this.message = message;
        this.deletedUserId = deletedUserId;
        this.timestamp = LocalDateTime.now();
        this.status = "200 OK";
    }


}
