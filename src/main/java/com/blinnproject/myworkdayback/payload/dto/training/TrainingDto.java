package com.blinnproject.myworkdayback.payload.dto.training;

import com.blinnproject.myworkdayback.model.entity.Training;
import com.blinnproject.myworkdayback.model.enums.EDayOfWeek;
import com.blinnproject.myworkdayback.model.enums.ETrainingStatus;
import com.blinnproject.myworkdayback.payload.dto.training_exercises.TrainingExercisesDto;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link Training}
 */
@Value
public class TrainingDto implements Serializable {
  Long id;
  Long createdBy;
  Long lastUpdatedBy;
  Instant createdOn;
  Instant lastUpdatedOn;
  Boolean isActive;
  String name;
  String sportPreset;
  EDayOfWeek trainingDays;
  ETrainingStatus trainingStatus;
  Date startDate;
  Date endDate;
  Date performedDate;
  String description;
  Boolean hasWarmUp;
  Boolean hasStretching;
  String iconName;
  String iconHexadecimalColor;
  TrainingDto parent;
  List<TrainingExercisesDto> trainingExercisesList;
}