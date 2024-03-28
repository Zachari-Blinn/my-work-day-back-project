package com.blinnproject.myworkdayback.payload.request;

import com.blinnproject.myworkdayback.model.entity.Series;
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

    private ArrayList<Series> series;
}
