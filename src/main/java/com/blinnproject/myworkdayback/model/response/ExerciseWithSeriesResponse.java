package com.blinnproject.myworkdayback.model.response;

import com.blinnproject.myworkdayback.model.entity.Exercise;
import com.blinnproject.myworkdayback.model.entity.WorkoutSet;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ExerciseWithSeriesResponse {
    private Exercise exercise;
    private List<WorkoutSet> series;

    public ExerciseWithSeriesResponse(Exercise exercise, List<WorkoutSet> series) {
        this.exercise = exercise;
        this.series = series;
    }
}
