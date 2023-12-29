package com.blinnproject.myworkdayback.payload.response;

import com.blinnproject.myworkdayback.model.EDayOfWeek;
import com.blinnproject.myworkdayback.model.ETrainingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormattedTrainingData {
  private long trainingId;
  private String trainingName;

  private ETrainingStatus trainingStatus;

  private ArrayList<EDayOfWeek> trainingDays;

  private String trainingIconName;
  private String trainingIconHexadecimalColor;

  private List<ExerciseData> trainingExercises;
  private int numberOfExercise;

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ExerciseData {
    private long exerciseId;
    private String exerciseName;
    private ExerciseState exerciseState = ExerciseState.NOT_STARTED;
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

