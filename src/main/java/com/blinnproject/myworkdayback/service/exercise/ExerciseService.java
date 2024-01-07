package com.blinnproject.myworkdayback.service.exercise;

import com.blinnproject.myworkdayback.model.Exercise;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public interface ExerciseService {
  Exercise create(Exercise exercise);

  List<Exercise> findAll(@NotNull Long createdBy);

  Optional<Exercise> findById(Long id);

}
