package com.example.test42task.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalFieldUpdateException extends RuntimeException {

    public IllegalFieldUpdateException(String message) {
        super(message);
    }
}
