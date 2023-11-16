package com.blinnproject.myworkdayback.payload.response;

import com.blinnproject.myworkdayback.model.User;
import lombok.Getter;

import java.util.List;

public class JwtResponse {
  private String token;
  @Getter
  private User user;
  private String type = "Bearer";
  private String refreshToken;
  @Getter
  private Long id;
  @Getter
  private String username;
  @Getter
  private String email;
  @Getter
  private final List<String> roles;

  public JwtResponse(String accessToken, String refreshToken, Long id, String username, String email, List<String> roles, User user) {
    this.token = accessToken;
    this.refreshToken = refreshToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.roles = roles;
    this.user = user;
  }

  public String getAccessToken() {
    return token;
  }

  public void setAccessToken(String accessToken) {
    this.token = accessToken;
  }

  public String getTokenType() {
    return type;
  }

  public void setTokenType(String tokenType) {
    this.type = tokenType;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}