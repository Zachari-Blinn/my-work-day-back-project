package com.blinnproject.myworkdayback.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public interface WorkoutModelSessionDateDTO {
  LocalDate getDate();
  Long getWorkoutModelId();
  String getWorkoutModelName();
  String getWorkoutModelIconName();
  String getWorkoutModelIconHexadecimalColor();
  LocalTime getStartTime();
  LocalTime getEndTime();
}
