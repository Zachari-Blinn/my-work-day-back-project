package com.blinnproject.myworkdayback.service.exercise;

import com.blinnproject.myworkdayback.exception.ResourceNotFoundException;
import com.blinnproject.myworkdayback.model.dto.ExerciseCreateDTO;
import com.blinnproject.myworkdayback.model.entity.Exercise;
import com.blinnproject.myworkdayback.repository.ExerciseRepository;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ExerciseServiceImpl implements ExerciseService {

  private final ExerciseRepository exerciseRepository;
  private final ModelMapper modelMapper;
  public ExerciseServiceImpl(ExerciseRepository exerciseRepository, ModelMapper modelMapper) {
    this.exerciseRepository = exerciseRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public Exercise create(ExerciseCreateDTO exerciseCreateDTO, Long createdBy) {
    Exercise exercise = modelMapper.map(exerciseCreateDTO, Exercise.class);
    exercise.setCreatedBy(createdBy);

    return exerciseRepository.save(exercise);
  }

  @Override
  public Optional<Exercise> findById(Long id, Long createdBy) {
    return exerciseRepository.findByIdAndCreatedByOrCreatedByIsNull(id, createdBy);
  }

  @Override
  public List<Exercise> findAll(Long createdBy) {
    return exerciseRepository.findAllByCreatedByOrCreatedByIsNull(createdBy);
  }

  @Override
  public Optional<Exercise> update(Long id, ExerciseCreateDTO exerciseCreateDTO, Long createdBy) {
    Optional<Exercise> exercise = exerciseRepository.findByIdAndCreatedBy(id, createdBy);

    if (exercise.isPresent()) {
      Exercise updatedExercise = modelMapper.map(exerciseCreateDTO, Exercise.class);
      updatedExercise.setId(id);
      updatedExercise.setCreatedBy(createdBy);

      return Optional.of(exerciseRepository.save(updatedExercise));
    }

    return Optional.empty();
  }

  @Override
  public void delete(Long id, Long createdBy) {
    Optional<Exercise> exercise = exerciseRepository.findByIdAndCreatedBy(id, createdBy);

    exercise.ifPresentOrElse(exerciseRepository::delete, () -> {
      throw new ResourceNotFoundException("Exercise", "id", id);
    });
  }
}
