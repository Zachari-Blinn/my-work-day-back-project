package com.blinnproject.myworkdayback.service.workout_exercise;

import com.blinnproject.myworkdayback.model.dto.WorkoutExerciseCreateDTO;
import com.blinnproject.myworkdayback.model.entity.Exercise;
import com.blinnproject.myworkdayback.model.entity.Workout;
import com.blinnproject.myworkdayback.model.entity.WorkoutExercise;
import com.blinnproject.myworkdayback.model.entity.WorkoutModel;

import java.util.ArrayList;
import java.util.Optional;

public interface WorkoutExerciseService {
  WorkoutExercise create(WorkoutExercise workoutExercise);

  Optional<WorkoutExercise> findByIdAndCreatedBy(Long id, Long createdBy);

  Optional<WorkoutExercise> delete(Long id, Long createdBy);

  ArrayList<WorkoutExercise> findAllByWorkoutId(Long id, Long createdBy);
}
