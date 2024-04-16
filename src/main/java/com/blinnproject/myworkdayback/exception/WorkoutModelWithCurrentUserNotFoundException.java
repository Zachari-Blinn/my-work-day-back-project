package com.blinnproject.myworkdayback.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class WorkoutModelWithCurrentUserNotFoundException extends RuntimeException {
  public WorkoutModelWithCurrentUserNotFoundException(String message) {
    super(message);
  }
}
