package com.blinnproject.myworkdayback.payload.request;

import com.blinnproject.myworkdayback.model.entity.TrainingExercises;
import lombok.*;

@Data
public class ModifyAndValidateRequest {
  private TrainingExercises[] trainingSession;
}
