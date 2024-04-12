package com.blinnproject.myworkdayback.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GenericResponse<T> {
  private boolean success;
  private String message;
  private T data;

  public static <T> GenericResponse<T> empty() {
    return success(null, null);
  }

  public static <T> GenericResponse<T> success(T data, String message) {
    return GenericResponse.<T>builder()
      .message(message)
      .data(data)
      .success(true)
      .build();
  }

  public static <T> GenericResponse<T> error(String message) {
    return GenericResponse.<T>builder()
      .message(message)
      .data(null)
      .success(false)
      .build();
  }
}
