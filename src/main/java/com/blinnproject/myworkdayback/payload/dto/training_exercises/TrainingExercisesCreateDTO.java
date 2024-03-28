package com.blinnproject.myworkdayback.payload.dto.training_exercises;

import com.blinnproject.myworkdayback.model.entity.WorkoutSet;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;

@Data
public class TrainingExercisesCreateDTO {

  @Min(0)
  int positionIndex;

  private String notes;

  @Min(0)
  private int numberOfWarmUpSeries;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date trainingDay;

  @Valid
  private ArrayList<WorkoutSet> workoutSets;
}
