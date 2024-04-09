package com.blinnproject.myworkdayback.service.workout_session;

import com.blinnproject.myworkdayback.model.entity.WorkoutSession;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkoutSessionService {
  List<Object[]> findAllByDate(LocalDateTime startedAt, Long createdBy);

  WorkoutSession createWorkoutSession(LocalDateTime startedAt, Long workoutModelId, Long createdBy);

  WorkoutSession find(Long id, Long createdBy);
}
