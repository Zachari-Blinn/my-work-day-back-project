package com.blinnproject.myworkdayback.payload.response;

import com.blinnproject.myworkdayback.model.enums.EDayOfWeek;
import com.blinnproject.myworkdayback.model.enums.ESessionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingExercisesSeriesInfo {
    private Long trainingId;
    private String trainingName;
    private ESessionStatus trainingStatus;
    private ArrayList<EDayOfWeek> trainingDays;
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
