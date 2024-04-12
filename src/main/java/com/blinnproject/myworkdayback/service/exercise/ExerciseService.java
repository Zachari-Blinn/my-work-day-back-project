package com.blinnproject.myworkdayback.service.exercise;

import com.blinnproject.myworkdayback.model.dto.ExerciseCreateDTO;
import com.blinnproject.myworkdayback.model.entity.Exercise;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public interface ExerciseService {
  Exercise create(ExerciseCreateDTO exerciseCreateDTO, Long createdBy);

  Optional<Exercise> findById(Long id, Long createdBy);

  List<Exercise> findAll(Long createdBy);

  Optional<Exercise> update(Long id, ExerciseCreateDTO exerciseCreateDTO, Long createdBy);

  void delete(Long id, Long createdBy);
}
