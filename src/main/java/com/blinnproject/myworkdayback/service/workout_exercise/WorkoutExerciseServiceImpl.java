package com.blinnproject.myworkdayback.service.workout_exercise;

import com.blinnproject.myworkdayback.model.dto.WorkoutExerciseCreateDTO;
import com.blinnproject.myworkdayback.model.entity.Exercise;
import com.blinnproject.myworkdayback.model.entity.Workout;
import com.blinnproject.myworkdayback.model.entity.WorkoutExercise;
import com.blinnproject.myworkdayback.repository.WorkoutExerciseRepository;
import com.blinnproject.myworkdayback.service.mapper.WorkoutExerciseMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class WorkoutExerciseServiceImpl implements WorkoutExerciseService {
  private final WorkoutExerciseRepository workoutExerciseRepository;
  private final WorkoutExerciseMapper workoutExerciseMapper;

  public WorkoutExerciseServiceImpl(WorkoutExerciseRepository workoutExerciseRepository, WorkoutExerciseMapper workoutExerciseMapper) {
    this.workoutExerciseRepository = workoutExerciseRepository;
    this.workoutExerciseMapper = workoutExerciseMapper;
  }

  @Override
  public WorkoutExercise create(WorkoutExercise workoutExercise) {
    return workoutExerciseRepository.save(workoutExercise);
  }

  @Override
  public Optional<WorkoutExercise> findByIdAndCreatedBy(Long id, Long createdBy) {
    return workoutExerciseRepository.findByIdAndCreatedBy(id, createdBy);
  }

  @Override
  public Optional<WorkoutExercise> delete(Long id, Long createdBy) {
    return workoutExerciseRepository.findByIdAndCreatedBy(id, createdBy
    ).map(workoutExercise -> {
      workoutExerciseRepository.delete(workoutExercise);
      return Optional.of(workoutExercise);
    }).orElseGet(Optional::empty);
  }

  @Override
  public ArrayList<WorkoutExercise> findAllByWorkoutId(Long id, Long createdBy) {
    return workoutExerciseRepository.findAllByWorkoutIdAndCreatedBy(id, createdBy);
  }

  @Override
  public Optional<WorkoutExercise> findByWorkoutSetId(Long workoutSetId, Long createdBy) {
    return workoutExerciseRepository.findOneByWorkoutSetsIdAndCreatedBy(workoutSetId, createdBy);
  }
}
