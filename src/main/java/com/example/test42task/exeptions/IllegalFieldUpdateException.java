package com.example.test42task.exeptions;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalFieldUpdateException extends RuntimeException {

    private Long userId;

    public IllegalFieldUpdateException(String message, Long userId) {
        super(message);
        this.userId = userId;
    }
}
