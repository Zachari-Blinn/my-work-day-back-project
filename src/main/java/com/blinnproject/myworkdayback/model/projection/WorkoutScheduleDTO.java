package com.blinnproject.myworkdayback.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.LocalTime;

public interface WorkoutScheduleDTO {
  @JsonIgnore
  LocalDate getRawDate();
  Long getWorkoutModelId();
  String getWorkoutModelName();
  String getWorkoutModelIconName();
  String getWorkoutModelIconHexadecimalColor();
  @JsonIgnore
  LocalTime getRawStartTime();
  @JsonIgnore
  LocalTime getRawEndTime();

  default String getDate() {
    return getRawDate().toString();
  }

  default String getStartTime() {
    return getRawStartTime().toString();
  }

  default String getEndTime() {
    return getRawEndTime().toString();
  }

}
