package com.blinnproject.myworkdayback.model.request;

import com.blinnproject.myworkdayback.model.entity.WorkoutSet;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.ArrayList;

@Data
public class AddExerciseRequest {

    @NotNull
    private Long exerciseId;

    @Min(0)
    private int numberOfWarmUpSeries;

    private String notes;

    private ArrayList<WorkoutSet> series;
}
