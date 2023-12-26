package com.blinnproject.myworkdayback.payload.request;

import com.blinnproject.myworkdayback.model.Series;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.ArrayList;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddExerciseRequest {

    @NotNull(message = "Exercise ID cannot be null")
    private Long exerciseId;

    private int numberOfWarmUpSeries;

    private String notes;

    private ArrayList<Series> series;
}
