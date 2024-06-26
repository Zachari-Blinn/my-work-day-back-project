package com.blinnproject.myworkdayback.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Date;

@Getter
@AllArgsConstructor
public class ErrorDetails {
  private Date timestamp;
  private String message;
  private String details;
}
