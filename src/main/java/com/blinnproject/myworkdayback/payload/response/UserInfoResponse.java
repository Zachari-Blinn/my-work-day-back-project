package com.blinnproject.myworkdayback.payload.response;

import lombok.Getter;

import java.util.List;

@Getter
public class UserInfoResponse {
  private Long id;
  private String username;
  private String email;
  private final String role;

  public UserInfoResponse(Long id, String username, String email, String role) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.role = role;
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

}