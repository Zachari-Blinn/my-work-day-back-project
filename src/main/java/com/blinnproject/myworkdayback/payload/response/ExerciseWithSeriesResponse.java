package com.blinnproject.myworkdayback.payload.response;

import com.blinnproject.myworkdayback.model.Exercise;
import com.blinnproject.myworkdayback.model.Series;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ExerciseWithSeriesResponse {
    private Exercise exercise;
    private List<Series> series;

    public ExerciseWithSeriesResponse(Exercise exercise, List<Series> series) {
        this.exercise = exercise;
        this.series = series;
    }
}
