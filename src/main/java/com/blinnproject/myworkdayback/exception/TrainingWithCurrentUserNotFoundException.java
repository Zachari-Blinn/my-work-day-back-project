package com.blinnproject.myworkdayback.exception;

public class TrainingWithCurrentUserNotFoundException extends RuntimeException {
    public TrainingWithCurrentUserNotFoundException(String message) {
        super(message);
    }
}
