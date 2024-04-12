package com.blinnproject.myworkdayback.model.dto;

import com.blinnproject.myworkdayback.model.entity.WorkoutSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

/**
 * DTO for {@link com.blinnproject.myworkdayback.model.entity.WorkoutExercise}
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkoutExerciseCreateDTO {
  private int positionIndex;

  private String notes;

  private int numberOfWarmUpSets;

  private ArrayList<WorkoutSet> workoutSets;
}
