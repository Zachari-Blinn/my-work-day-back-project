package com.blinnproject.myworkdayback.payload.request;

import jakarta.validation.constraints.NotBlank;

import java.time.DayOfWeek;
import java.util.Arrays;

public class CreateTrainingRequest {
  @NotBlank
  private String name;

  private String sportPreset;

  private DayOfWeek[] trainingDays;

  private String description;

  private Boolean hasWarpUp;

  private Boolean hasStretching;
}
