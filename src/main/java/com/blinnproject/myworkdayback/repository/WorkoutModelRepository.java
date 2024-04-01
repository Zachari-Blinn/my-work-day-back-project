package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.entity.WorkoutModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface WorkoutModelRepository extends JpaRepository<WorkoutModel, Long> {
  Optional<WorkoutModel> findByName(String name);

  Optional<WorkoutModel> findByIdAndCreatedBy(Long id, Long createdBy);

  ArrayList<WorkoutModel> findAllByCreatedBy(Long createdBy);

//  @Query("""
//    SELECT workoutModel
//      FROM WorkoutModel workoutModel
//      JOIN workoutModel.id ON
//      WHERE workoutModel.id = :workoutModelId AND workoutModel.createdBy = :createdBy
//  """)
//  ScopedValue<Object> findByIdWithExercises(Long workoutModelId, Long createdBy);
}