package com.blinnproject.myworkdayback.payload.response;

import com.blinnproject.myworkdayback.model.Exercise;
import com.blinnproject.myworkdayback.model.Series;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ExerciseWithSeriesResponse {
    private Exercise exercise;
    private Set<Series> series;

    public ExerciseWithSeriesResponse(Exercise exercise, Set<Series> series) {
        this.exercise = exercise;
        this.series = series;
    }
}
