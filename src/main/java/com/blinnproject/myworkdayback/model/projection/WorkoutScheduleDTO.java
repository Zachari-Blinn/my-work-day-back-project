package com.blinnproject.myworkdayback.model.projection;

import java.time.LocalDate;
import java.time.LocalTime;

public interface WorkoutScheduleDTO {
  LocalDate getDate();
  Long getWorkoutModelId();
  String getWorkoutModelName();
  String getWorkoutModelIconName();
  String getWorkoutModelIconHexadecimalColor();
  LocalTime getStartTime();
  LocalTime getEndTime();
}
