package com.blinnproject.myworkdayback.payload.projection;

import com.blinnproject.myworkdayback.model.enums.EDayOfWeek;
import com.blinnproject.myworkdayback.model.enums.ESessionStatus;

import java.util.ArrayList;

public interface TrainingExercisesSeriesInfoProjection {
  Long getTrainingId();
  String getTrainingName();
  Short getRawTrainingStatus();
  ArrayList<Short> getRawTrainingDays();
  String getTrainingIconName();
  String getTrainingIconHexadecimalColor();
  Long getExerciseId();
  String getExerciseName();
  Long getSeriesId();
  int getSeriesPositionIndex();
  int getSeriesRepsCount();
  String getSeriesRestTime();
  int getSeriesWeight();
  boolean getIsCompleted();

  // format enum

  default ESessionStatus getTrainingStatus() {
    return (getRawTrainingStatus() != null) ? ESessionStatus.of(getRawTrainingStatus()) : null;
  }

  default ArrayList<EDayOfWeek> getTrainingDays() {
    ArrayList<Short> rawTrainingDays = getRawTrainingDays();
    ArrayList<EDayOfWeek> trainingDays = new ArrayList<>();

    for (Short rawDay : rawTrainingDays) {
      trainingDays.add(EDayOfWeek.of(rawDay.intValue()));
    }

    return trainingDays;
  }
}
