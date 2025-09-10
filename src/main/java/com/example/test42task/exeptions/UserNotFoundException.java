package com.example.test42task.exeptions;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException{
    private final Long userId;

    public UserNotFoundException(Long userId) {
        super("User not found with id: " + userId);
        this.userId = userId;
    }

}
