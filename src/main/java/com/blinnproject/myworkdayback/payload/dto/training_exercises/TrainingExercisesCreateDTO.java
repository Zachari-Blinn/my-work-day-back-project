package com.blinnproject.myworkdayback.payload.dto.training_exercises;

import com.blinnproject.myworkdayback.model.Series;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class TrainingExercisesCreateDTO {
  private String notes;

  private int numberOfWarmUpSeries;

  private Date trainingDay;

  @Valid
  private ArrayList<Series> series;
}
