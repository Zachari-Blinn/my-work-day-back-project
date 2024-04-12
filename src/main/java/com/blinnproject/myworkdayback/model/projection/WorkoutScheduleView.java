package com.blinnproject.myworkdayback.model.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.LocalTime;

public interface WorkoutScheduleView {
  @JsonFormat(pattern = "yyyy-MM-dd")
  LocalDate getDate();
  Long getWorkoutModelId();
  String getWorkoutModelName();
  String getWorkoutModelIconName();
  String getWorkoutModelIconHexadecimalColor();
  @JsonFormat(pattern = "HH:mm:ss")
  LocalTime getStartTime();
  @JsonFormat(pattern = "HH:mm:ss")
  LocalTime getEndTime();

}
