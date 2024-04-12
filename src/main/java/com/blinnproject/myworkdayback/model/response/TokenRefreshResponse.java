package com.blinnproject.myworkdayback.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class TokenRefreshResponse {
  private String accessToken;
  private String refreshToken;
  private String tokenType = "Bearer";
  private Instant expirationDate;

  public TokenRefreshResponse(String accessToken, String refreshToken, Instant expirationDate) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expirationDate = expirationDate;
  }

}