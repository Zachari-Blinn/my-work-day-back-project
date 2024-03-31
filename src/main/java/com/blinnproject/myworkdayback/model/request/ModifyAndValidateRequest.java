package com.blinnproject.myworkdayback.model.request;

import com.blinnproject.myworkdayback.model.entity.WorkoutExercise;
import lombok.*;

@Data
public class ModifyAndValidateRequest {
  private WorkoutExercise[] trainingSession;
}
