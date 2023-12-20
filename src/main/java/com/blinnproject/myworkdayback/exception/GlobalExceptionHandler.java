package com.blinnproject.myworkdayback.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({EmailAlreadyTakenException.class})
  public ResponseEntity<Object> handleEmailAlreadyTakenException(EmailAlreadyTakenException exception) {
    return ResponseEntity
      .status(HttpStatus.CONFLICT)
      .body(exception.getMessage());
  }

  @ExceptionHandler({UsernameAlreadyTakenException.class})
  public ResponseEntity<Object> handleUsernameAlreadyTakenException(UsernameAlreadyTakenException exception) {
    return ResponseEntity
      .status(HttpStatus.CONFLICT)
      .body(exception.getMessage());
  }

  @ExceptionHandler({TokenRefreshException.class})
  public ResponseEntity<Object> handleTokenRefreshException(TokenRefreshException exception) {
    return ResponseEntity
      .status(HttpStatus.FORBIDDEN)
      .body(exception.getMessage());
  }

}
