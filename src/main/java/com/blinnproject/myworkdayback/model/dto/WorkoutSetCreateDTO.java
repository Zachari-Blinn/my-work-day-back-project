package com.blinnproject.myworkdayback.model.dto;

import com.blinnproject.myworkdayback.model.entity.WorkoutSet;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link WorkoutSet}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkoutSetCreateDTO {
  private int positionIndex;
  private int weight;
  private String restTime;
  private String notes;
  private int repsCount;
}