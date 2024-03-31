package com.blinnproject.myworkdayback.model.dto;

import com.blinnproject.myworkdayback.constraint.HexadecimalColorConstraint;
import com.blinnproject.myworkdayback.constraint.IconNameConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * DTO for {@link com.blinnproject.myworkdayback.model.entity.WorkoutModel}
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkoutModelCreateDTO {
  @NotBlank
  @Size(min = 2, message = "name should have at least 2 characters")
  private String name;

  private String description;

  private String sportPreset;

  private Boolean hasWarmUp;

  private Boolean hasStretching;

  @IconNameConstraint
  private String iconName;

  @HexadecimalColorConstraint
  private String iconHexadecimalColor;
}