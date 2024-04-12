package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.entity.WorkoutSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkoutSetRepository extends JpaRepository<WorkoutSet, Long> {
  Optional<WorkoutSet> findByIdAndCreatedBy(Long workoutSetId, Long createdBy);
}
