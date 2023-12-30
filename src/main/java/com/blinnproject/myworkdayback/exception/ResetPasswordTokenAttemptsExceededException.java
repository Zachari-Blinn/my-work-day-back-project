package com.blinnproject.myworkdayback.exception;

public class ResetPasswordTokenAttemptsExceededException extends RuntimeException {
  public ResetPasswordTokenAttemptsExceededException(String message) {
    super(message);
  }
}
