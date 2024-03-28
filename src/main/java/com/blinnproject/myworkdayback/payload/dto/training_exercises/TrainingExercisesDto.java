package com.blinnproject.myworkdayback.payload.dto.training_exercises;

import com.blinnproject.myworkdayback.model.entity.WorkoutExercise;
import com.blinnproject.myworkdayback.payload.dto.exercise.ExerciseDto;
import com.blinnproject.myworkdayback.payload.dto.series.SeriesDto;
import com.blinnproject.myworkdayback.payload.dto.training.TrainingDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link WorkoutExercise}
 */
@Value
public class TrainingExercisesDto implements Serializable {
  Long id;
  @NotNull
  @Min(0)
  int positionIndex;
  TrainingExercisesDto parent;
  TrainingDto training;
  ExerciseDto exercise;
  List<SeriesDto> seriesList;
  String notes;
  @Min(0)
  int numberOfWarmUpSeries;
  Date trainingDay;
}