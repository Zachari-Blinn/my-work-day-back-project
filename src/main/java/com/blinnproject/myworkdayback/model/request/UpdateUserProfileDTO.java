package com.blinnproject.myworkdayback.model.request;

import com.blinnproject.myworkdayback.model.enums.EGender;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateUserProfileDTO {
  @Nullable
  private String username;

  @Nullable
  @Email
  private String email;

  @Nullable
  private EGender gender;
}
