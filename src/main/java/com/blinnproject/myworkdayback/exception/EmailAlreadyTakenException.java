package com.blinnproject.myworkdayback.exception;

public class EmailAlreadyTakenException extends RuntimeException {
  public EmailAlreadyTakenException(String message) {
    super(message);
  }
}
