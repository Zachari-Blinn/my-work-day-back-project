package com.blinnproject.myworkdayback.payload.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotNull
  @Min(0)
  @Max(9999)
  private int token;

  @NotBlank
  @Size(min = 6, max = 40)
  private String newPassword;
}
