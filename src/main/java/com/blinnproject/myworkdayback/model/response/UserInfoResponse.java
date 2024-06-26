package com.blinnproject.myworkdayback.model.response;

import com.blinnproject.myworkdayback.model.enums.EGender;
import com.blinnproject.myworkdayback.model.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponse {
  private Long id;
  private String username;
  private String email;
  private EGender gender;

  public UserInfoResponse(User user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.email = user.getEmail();
    this.gender = user.getGender();
  }
}