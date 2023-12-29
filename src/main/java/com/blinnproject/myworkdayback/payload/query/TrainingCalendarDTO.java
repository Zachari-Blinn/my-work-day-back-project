package com.blinnproject.myworkdayback.payload.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingCalendarDTO {
  private String date;
  private List<TrainingDTO> trainings;

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class TrainingDTO {
    private String trainingName;
    private String trainingColor;
  }
}
