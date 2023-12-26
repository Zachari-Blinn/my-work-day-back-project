package com.blinnproject.myworkdayback.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.DayOfWeek;
import java.util.ArrayList;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTrainingRequest {
  @NotBlank
  private String name;

  private String sportPreset;

  private ArrayList<DayOfWeek> trainingDays;

  private String description;

  private Boolean hasWarpUp;

  private Boolean hasStretching;
}
