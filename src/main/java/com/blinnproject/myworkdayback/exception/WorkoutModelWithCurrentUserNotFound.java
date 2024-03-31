package com.blinnproject.myworkdayback.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class WorkoutModelWithCurrentUserNotFound extends RuntimeException {
  public WorkoutModelWithCurrentUserNotFound(String message) {
    super(message);
  }
}
