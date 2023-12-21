package com.blinnproject.myworkdayback.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormattedTrainingData {
  private long trainingId;
  private String trainingName;
  private List<ExerciseData> trainingExercises;
  private int numberOfExercise;

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ExerciseData {
    private long exerciseId;
    private String exerciseName;
    private List<SeriesEntry> series;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SeriesEntry {
      private long id;
      private int positionIndex;
      private int repsCount;
      private String restTime;
      private double weight;
      private boolean completed;
    }
  }
}
