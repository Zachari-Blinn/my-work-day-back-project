package com.blinnproject.myworkdayback.service.workout_exercise;

import com.blinnproject.myworkdayback.model.entity.WorkoutExercise;

import java.util.ArrayList;
import java.util.Optional;

public interface WorkoutExerciseService {
  WorkoutExercise create(WorkoutExercise workoutExercise);

  Optional<WorkoutExercise> findByIdAndCreatedBy(Long id, Long createdBy);

  Optional<WorkoutExercise> delete(Long id, Long createdBy);

  ArrayList<WorkoutExercise> findAllByWorkoutId(Long id, Long createdBy);

  Optional<WorkoutExercise> findByWorkoutSetId(Long workoutSetId, Long createdBy);
}
