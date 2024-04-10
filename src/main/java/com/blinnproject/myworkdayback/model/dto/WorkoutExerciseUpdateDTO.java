package com.blinnproject.myworkdayback.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkoutExerciseUpdateDTO {
  private int positionIndex;

  private String notes;

  private int numberOfWarmUpSets;
}
