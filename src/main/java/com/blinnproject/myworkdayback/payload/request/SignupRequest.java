package com.blinnproject.myworkdayback.payload.request;

import com.blinnproject.myworkdayback.model.enums.EGender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class SignupRequest {
  @NotBlank
  @Size(min = 3, max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email(flags = Pattern.Flag.CASE_INSENSITIVE)
  private String email;

  private EGender gender;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;
}