package com.blinnproject.myworkdayback.service.exercise;

import com.blinnproject.myworkdayback.model.entity.Exercise;
import com.blinnproject.myworkdayback.repository.ExerciseRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ExerciseServiceImpl implements ExerciseService {

  private final ExerciseRepository exerciseRepository;

  public ExerciseServiceImpl(ExerciseRepository exerciseRepository) {
    this.exerciseRepository = exerciseRepository;
  }

  @Override
  public Exercise create(Exercise exercise) {
    return exerciseRepository.save(exercise);
  }

  @Override
  public List<Exercise> findAll(@NotNull Long createdBy) {
    return exerciseRepository.findAllByCreatedByOrCreatedByIsNull(createdBy);
  }

  @Override
  public Optional<Exercise> findById(Long id) {
    return exerciseRepository.findById(id);
  }
}
