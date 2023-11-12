package com.blinnproject.myworkdayback.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TokenRefreshRequest {
  @NotBlank
  private String refreshToken;

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}