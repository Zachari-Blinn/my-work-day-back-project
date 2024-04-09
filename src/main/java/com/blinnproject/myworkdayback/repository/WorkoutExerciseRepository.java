package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.entity.Exercise;
import com.blinnproject.myworkdayback.model.entity.Workout;
import com.blinnproject.myworkdayback.model.entity.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;


@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long> {
  Optional<WorkoutExercise> findByIdAndCreatedBy(Long id, Long createdBy);

  ArrayList<WorkoutExercise> findAllByWorkoutIdAndCreatedBy(Long id, Long createdBy);

  Optional<WorkoutExercise> findOneByWorkoutSetsId(Long workoutSetId);
}
