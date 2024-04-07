package com.blinnproject.myworkdayback.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutSessionsScheduleDTO {
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate date;

  private long workoutModelId;

  private String workoutModelName;

  private String workoutModelIconName;

  private String workoutModelIconHexadecimalColor;

  @JsonFormat(pattern = "HH:mm")
  private LocalTime startTime;

  @JsonFormat(pattern = "HH:mm")
  private LocalTime endTime;

  private String sessionStatus;

  private ArrayList<WorkoutSessionListDTO> workoutSessionList;

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class WorkoutSessionListDTO {
    private long workoutSessionId;

    private String status;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
  }
}

