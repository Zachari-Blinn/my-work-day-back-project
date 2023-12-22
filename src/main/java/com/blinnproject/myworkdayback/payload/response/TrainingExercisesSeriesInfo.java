package com.blinnproject.myworkdayback.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingExercisesSeriesInfo {
    private Long trainingId;
    private String trainingName;

    private String trainingIconName;
    private String trainingIconHexadecimalColor;

    private Long exerciseId;
    private String exerciseName;

    private Long seriesId;
    private int seriesPositionIndex;
    private int seriesRepsCount;
    private String seriesRestTime;
    private int seriesWeight;
    private boolean isCompleted;
}
