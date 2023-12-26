package com.blinnproject.myworkdayback.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateTrainingRequest {
    @NotNull(message = "Training day cannot be null")
    private Date trainingDay;
}
