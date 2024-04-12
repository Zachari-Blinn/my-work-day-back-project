package com.blinnproject.myworkdayback.model.dto;

import com.blinnproject.myworkdayback.model.enums.EMuscle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

/**
 * DTO for {@link com.blinnproject.myworkdayback.model.entity.Exercise}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExerciseCreateDTO {
  @NotBlank
  @Size(min = 2, message = "name should have at least 2 characters")
  private String name;

  private Set<EMuscle> musclesUsed;
}