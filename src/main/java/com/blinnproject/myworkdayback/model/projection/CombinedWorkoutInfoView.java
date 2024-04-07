package com.blinnproject.myworkdayback.model.projection;

import java.time.LocalDate;
import java.time.LocalTime;

public interface CombinedWorkoutInfoView {
  LocalDate getDate();
  String getWorkoutType();
  Long getWorkoutModelId();
  Long getWorkoutSessionId();
  String getWorkoutModelName();
  String getWorkoutModelIconName();
  String getWorkoutModelIconHexadecimalColor();
  LocalTime getStartTime();
  LocalTime getEndTime();
  String getSessionStatus();
}
