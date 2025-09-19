package com.example.test42task.exeptions;

public class UnknownParameterException extends RuntimeException{
    private Long userId;

    public UnknownParameterException(Long userId){
        this.userId = userId;

    }
}
