package com.blinnproject.myworkdayback.service.workout_session;

import com.blinnproject.myworkdayback.model.entity.WorkoutSession;
import com.blinnproject.myworkdayback.model.entity.WorkoutSet;

import java.time.LocalDateTime;

public interface WorkoutSessionService {
  WorkoutSession createWorkoutSession(LocalDateTime startedAt, Long workoutModelId, Long createdBy);

  WorkoutSession find(Long id, Long createdBy);

  WorkoutSession updateWorkoutSessionSet(Long workoutSetId, WorkoutSet workoutSet, Long createdBy);

  WorkoutSession createWorkoutSessionManually(LocalDateTime startedAt, LocalDateTime endedAt, Long workoutModelId, Long createdBy);

  void delete(Long id, Long createdBy);
}
