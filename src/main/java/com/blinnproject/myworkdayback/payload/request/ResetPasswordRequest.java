package com.blinnproject.myworkdayback.payload.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class ResetPasswordRequest {

  @NotBlank
  @Size(max = 50)
  @Email(flags = Pattern.Flag.CASE_INSENSITIVE)
  private String email;

  @NotNull
  @Min(0)
  @Max(999999)
  private int token;

  @NotBlank
  @Size(min = 6, max = 40)
  private String newPassword;
}
