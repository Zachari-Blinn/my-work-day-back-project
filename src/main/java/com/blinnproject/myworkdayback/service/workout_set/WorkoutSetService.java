package com.blinnproject.myworkdayback.service.workout_set;

import com.blinnproject.myworkdayback.model.entity.WorkoutSet;

import java.util.Optional;

public interface WorkoutSetService {
  Optional<WorkoutSet> findById(Long id, Long createdBy);

  WorkoutSet update(long id, WorkoutSet workoutSet, Long createdBy);
}
