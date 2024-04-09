package com.blinnproject.myworkdayback.service.workout_session;

import com.blinnproject.myworkdayback.exception.ResourceNotFoundException;
import com.blinnproject.myworkdayback.model.entity.WorkoutExercise;
import com.blinnproject.myworkdayback.model.entity.WorkoutModel;
import com.blinnproject.myworkdayback.model.entity.WorkoutSession;
import com.blinnproject.myworkdayback.model.entity.WorkoutSet;
import com.blinnproject.myworkdayback.model.enums.EPerformStatus;
import com.blinnproject.myworkdayback.model.enums.ESessionStatus;
import com.blinnproject.myworkdayback.repository.WorkoutExerciseRepository;
import com.blinnproject.myworkdayback.repository.WorkoutSessionRepository;
import com.blinnproject.myworkdayback.repository.WorkoutSetRepository;
import com.blinnproject.myworkdayback.service.workout_model.WorkoutModelService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WorkoutSessionServiceImpl implements WorkoutSessionService {

  private final WorkoutSessionRepository workoutSessionRepository;
  private final WorkoutModelService workoutModelService;
  private final WorkoutSetRepository workoutSetRepository;
  private final WorkoutExerciseRepository workoutExerciseRepository;

  public WorkoutSessionServiceImpl(WorkoutSessionRepository workoutSessionRepository, WorkoutModelService workoutModelService, WorkoutSetRepository workoutSetRepository, WorkoutExerciseRepository workoutExerciseRepository) {
    this.workoutSessionRepository = workoutSessionRepository;
    this.workoutModelService = workoutModelService;
    this.workoutSetRepository = workoutSetRepository;
    this.workoutExerciseRepository = workoutExerciseRepository;
  }

  @Override
  public List<Object[]> findAllByDate(LocalDateTime startedAt, Long createdBy) {
    return workoutSessionRepository.findAllSessionByDate(2, startedAt, createdBy);
  }

  @Override
  public WorkoutSession createWorkoutSession(LocalDateTime startedAt, Long workoutModelId, Long createdBy) {
    // check if workout model exists with user id and get it
    WorkoutModel workoutModel = workoutModelService.findById(workoutModelId, createdBy).orElseThrow(ResourceNotFoundException::new);

    // clone workout model and workout exercises associated with it
    WorkoutSession workoutSession = new WorkoutSession();
    workoutSession.setName(workoutModel.getName());
    workoutSession.setWorkoutModel(workoutModel);
    workoutSession.setSessionStatus(ESessionStatus.IN_PROGRESS);
    workoutSession.setStartedAt(startedAt);

    List<WorkoutExercise> workoutExercises = new ArrayList<>();
    for (WorkoutExercise workoutExerciseIn : workoutModel.getWorkoutExerciseList()) {

      WorkoutExercise workoutExercise = new WorkoutExercise();
      workoutExercise.setExercise(workoutExerciseIn.getExercise());
      workoutExercise.setWorkout(workoutSession);
      workoutExercise.setPositionIndex(workoutExerciseIn.getPositionIndex());
      workoutExercise.setPerformStatus(EPerformStatus.NOT_PERFORMED);
      workoutExercise.setCreatedBy(createdBy);
      workoutExercise.setNumberOfWarmUpSets(workoutExerciseIn.getNumberOfWarmUpSets());

      List<WorkoutSet> workoutSets = new ArrayList<>();
      for (WorkoutSet workoutSetIn : workoutExerciseIn.getWorkoutSets()) {
        WorkoutSet workoutSet = new WorkoutSet();
        workoutSet.setWeight(workoutSetIn.getWeight());
        workoutSet.setCreatedBy(createdBy);
        workoutSet.setRestTime(workoutSetIn.getRestTime());
        workoutSet.setRepsCount(workoutSetIn.getRepsCount());
        workoutSet.setIsPerformed(false);
        workoutSet.setPositionIndex(workoutSetIn.getPositionIndex());

        workoutSets.add(workoutSet);
      }
      workoutExercise.setWorkoutSets(workoutSets);

      workoutExercises.add(workoutExercise);
    }
    workoutSession.setWorkoutExerciseList(workoutExercises);

    workoutSession.setCreatedBy(createdBy);
    workoutSession.setEndedAt(null);

    return workoutSessionRepository.save(workoutSession);
  }

  @Override
  public WorkoutSession find(Long id, Long createdBy) {
    return workoutSessionRepository.findByIdAndCreatedBy(id, createdBy).orElseThrow(ResourceNotFoundException::new);
  }

  @Override
  public WorkoutSession updateWorkoutSessionSet(Long id, Long workoutSetId, WorkoutSet workoutSet, Long createdBy) {
    WorkoutSet workoutSetIn = workoutSetRepository.findByIdAndCreatedBy(workoutSetId, createdBy).orElseThrow(() -> new ResourceNotFoundException("WorkoutSet", "id", workoutSetId));

    workoutSetIn.setWeight(workoutSet.getWeight());
    workoutSetIn.setRepsCount(workoutSet.getRepsCount());
    workoutSetIn.setRestTime(workoutSet.getRestTime());
    workoutSetIn.setIsPerformed(workoutSet.getIsPerformed());
    workoutSetIn.setPositionIndex(workoutSet.getPositionIndex());
    workoutSetRepository.saveAndFlush(workoutSetIn);

    WorkoutExercise workoutExercise = workoutExerciseRepository.findOneByWorkoutSetsId(workoutSetId).orElseThrow(() -> new ResourceNotFoundException("WorkoutExercise", "id", workoutSetId));

    boolean isAllPerformed = workoutExercise.getWorkoutSets().stream().allMatch(WorkoutSet::getIsPerformed);

    if (isAllPerformed) {
      workoutExercise.setPerformStatus(EPerformStatus.COMPLETED);
    } else {
      workoutExercise.setPerformStatus(EPerformStatus.IN_PROGRESS);
    }
    workoutExerciseRepository.saveAndFlush(workoutExercise);

    return workoutSessionRepository.findByIdAndCreatedBy(id, createdBy).orElseThrow(ResourceNotFoundException::new);
  }
}
