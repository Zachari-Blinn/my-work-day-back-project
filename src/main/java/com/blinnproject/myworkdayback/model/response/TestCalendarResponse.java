package com.blinnproject.myworkdayback.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestCalendarResponse {
  private Date calendar_day;
  private List<TrainingData> training_data;

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class TrainingData {
    private String training_name;
    private String training_color;
  }
}
