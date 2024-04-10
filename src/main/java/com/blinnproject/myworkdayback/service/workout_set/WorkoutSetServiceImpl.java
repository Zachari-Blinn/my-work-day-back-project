package com.blinnproject.myworkdayback.service.workout_set;

import com.blinnproject.myworkdayback.model.entity.WorkoutSet;
import com.blinnproject.myworkdayback.repository.WorkoutSetRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorkoutSetServiceImpl implements WorkoutSetService {

  private final WorkoutSetRepository workoutSetRepository;

  public WorkoutSetServiceImpl(WorkoutSetRepository workoutSetRepository) {
    this.workoutSetRepository = workoutSetRepository;
  }

  @Override
  public Optional<WorkoutSet> findById(Long id, Long createdBy) {
    return workoutSetRepository.findByIdAndCreatedBy(id, createdBy);
  }

  @Override
  public WorkoutSet update(long id, WorkoutSet workoutSetData, Long createdBy) {
    WorkoutSet workoutSetIn = workoutSetRepository.findByIdAndCreatedBy(id, createdBy).orElseThrow(() -> new RuntimeException("Workout set not found"));

    workoutSetIn.setWeight(workoutSetData.getWeight());
    workoutSetIn.setRepsCount(workoutSetData.getRepsCount());
    workoutSetIn.setRestTime(workoutSetData.getRestTime());
    workoutSetIn.setIsPerformed(workoutSetData.getIsPerformed());
    workoutSetIn.setPositionIndex(workoutSetData.getPositionIndex());

    return workoutSetRepository.save(workoutSetIn);
  }
}
