package com.blinnproject.myworkdayback.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class JwtResponse {
  private String accessToken;
  private String tokenType = "Bearer";
  private String refreshToken;
  private Instant expirationDate;
  private Long id;
  private String username;
  private String email;
  private final List<String> roles;

  public JwtResponse(String accessToken, String refreshToken, Instant expirationDate, Long id, String username, String email, List<String> roles) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expirationDate = expirationDate;
    this.id = id;
    this.username = username;
    this.email = email;
    this.roles = roles;
  }
}