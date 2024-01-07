package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.Exercise;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

  List<Exercise> findAllByCreatedByOrCreatedByIsNull(@NotNull Long createdBy);
}
