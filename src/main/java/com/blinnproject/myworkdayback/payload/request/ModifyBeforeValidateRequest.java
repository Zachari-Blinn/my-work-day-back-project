package com.blinnproject.myworkdayback.payload.request;

import com.blinnproject.myworkdayback.model.TrainingExercises;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyBeforeValidateRequest {
  private TrainingExercises[] trainingSession;
}
