package com.blinnproject.myworkdayback.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserPasswordDTO {
  @NotBlank
  private String oldPassword;

  @NotBlank
  private String newPassword;
}
