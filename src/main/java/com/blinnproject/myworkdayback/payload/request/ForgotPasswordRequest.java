package com.blinnproject.myworkdayback.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class ForgotPasswordRequest {
  @NotBlank
  @Size(max = 50)
  @Email(flags = Pattern.Flag.CASE_INSENSITIVE)
  private String email;
}
