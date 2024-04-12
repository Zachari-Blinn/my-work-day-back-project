package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.entity.Exercise;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
  @Query("""
    SELECT exercise
    FROM Exercise exercise
    WHERE exercise.id = :id
    AND (exercise.createdBy = :createdBy OR exercise.createdBy IS NULL)
  """)
  Optional<Exercise> findById(Long id, Long createdBy);

  List<Exercise> findAllByCreatedByOrCreatedByIsNull(@NotNull Long createdBy);

  Optional<Exercise> findByIdAndCreatedByOrCreatedByIsNull(Long id, Long createdBy);

  Optional<Exercise> findByIdAndCreatedBy(Long id, Long createdBy);
}
