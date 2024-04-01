package com.blinnproject.myworkdayback.service.workout_session;

import com.blinnproject.myworkdayback.model.entity.WorkoutSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface WorkoutSessionService {
  List<WorkoutSession> findAllByDate(LocalDate startedAt, Long createdBy);

  WorkoutSession createWorkoutSession(LocalDateTime startedAt, Long workoutModelId, Long createdBy);
}
