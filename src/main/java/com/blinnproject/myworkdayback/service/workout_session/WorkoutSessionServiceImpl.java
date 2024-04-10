package com.blinnproject.myworkdayback.service.workout_session;

import com.blinnproject.myworkdayback.exception.ProvidedResourceIsNotAnInstanceOf;
import com.blinnproject.myworkdayback.exception.ResourceNotFoundException;
import com.blinnproject.myworkdayback.model.entity.WorkoutExercise;
import com.blinnproject.myworkdayback.model.entity.WorkoutModel;
import com.blinnproject.myworkdayback.model.entity.WorkoutSession;
import com.blinnproject.myworkdayback.model.entity.WorkoutSet;
import com.blinnproject.myworkdayback.model.enums.EPerformStatus;
import com.blinnproject.myworkdayback.model.enums.ESessionStatus;
import com.blinnproject.myworkdayback.repository.WorkoutSessionRepository;
import com.blinnproject.myworkdayback.service.workout_exercise.WorkoutExerciseService;
import com.blinnproject.myworkdayback.service.workout_model.WorkoutModelService;
import com.blinnproject.myworkdayback.service.workout_set.WorkoutSetService;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkoutSessionServiceImpl implements WorkoutSessionService {

  private final WorkoutSessionRepository workoutSessionRepository;
  private final WorkoutModelService workoutModelService;
  private final WorkoutSetService workoutSetService;
  private final WorkoutExerciseService workoutExerciseService;

  public WorkoutSessionServiceImpl(WorkoutSessionRepository workoutSessionRepository, WorkoutModelService workoutModelService, WorkoutSetService workoutSetService, WorkoutExerciseService workoutExerciseService) {
    this.workoutSessionRepository = workoutSessionRepository;
    this.workoutModelService = workoutModelService;
    this.workoutSetService = workoutSetService;
    this.workoutExerciseService = workoutExerciseService;
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
  public WorkoutSession updateWorkoutSessionSet(Long workoutSetId, WorkoutSet workoutSetData, Long createdBy) {
    // check if workout set is associated with a workout exercise from a workout session
    WorkoutExercise workoutExercise = workoutExerciseService.findByWorkoutSetId(workoutSetId, createdBy)
      .orElseThrow(() -> new ResourceNotFoundException("WorkoutExercise", "id", workoutSetId));
    if (workoutExercise.belongsToModel()) {
      throw new ProvidedResourceIsNotAnInstanceOf("WorkoutSet", workoutSetId.toString(), "SESSION" );
    }

    workoutSetService.update(workoutSetId, workoutSetData, createdBy);

    // Vérifier si tous les workout_set de workout_exercise ont isPerformed = true
    boolean allWorkoutSetPerformed = workoutExercise.getWorkoutSets().stream().allMatch(WorkoutSet::getIsPerformed);
    if (allWorkoutSetPerformed) {
      workoutExercise.setPerformStatus(EPerformStatus.PERFORMED);
    } else {
      workoutExercise.setPerformStatus(EPerformStatus.IN_PROGRESS);
    }
    workoutExerciseService.create(workoutExercise);

    // Vérifier si tous les workout_exercise ont performStatus = "COMPLETED"
    List<WorkoutExercise> workoutExercises = workoutExerciseService.findAllByWorkoutId(workoutExercise.getWorkout().getId(), createdBy);
    boolean allExerciseCompleted = workoutExercises.stream()
      .allMatch(we -> we.getPerformStatus() == EPerformStatus.PERFORMED || we.getPerformStatus() == EPerformStatus.CANCELLED);

    // Mettre à jour le sessionStatus de la WorkoutSession
    if (allExerciseCompleted) {
      WorkoutSession workoutSession = (WorkoutSession) workoutExercise.getWorkout();
      workoutSession.setSessionStatus(ESessionStatus.PERFORMED);
      workoutSessionRepository.saveAndFlush(workoutSession);
    }

    return workoutSessionRepository.findByIdAndCreatedBy(workoutExercise.getWorkout().getId(), createdBy)
      .orElseThrow(ResourceNotFoundException::new);
  }

  @Override
  public WorkoutSession createWorkoutSessionManually(
    @NotNull LocalDateTime startedAt,
    @NotNull LocalDateTime endedAt,
    @NotNull Long workoutModelId,
    @NotNull Long createdBy
  ) {
    WorkoutModel workoutModel = workoutModelService.findById(workoutModelId, createdBy).orElseThrow(ResourceNotFoundException::new);

    // clone workout model and workout exercises associated with it
    WorkoutSession workoutSession = new WorkoutSession();
    workoutSession.setName(workoutModel.getName());
    workoutSession.setDescription(null);
    workoutSession.setWorkoutModel(workoutModel);
    workoutSession.setSessionStatus(ESessionStatus.PERFORMED);
    workoutSession.setStartedAt(startedAt);
    workoutSession.setEndedAt(endedAt);
    workoutSession.setCreatedBy(createdBy);

    List<WorkoutExercise> workoutExercises = new ArrayList<>();
    for (WorkoutExercise workoutExerciseIn : workoutModel.getWorkoutExerciseList()) {

      WorkoutExercise workoutExercise = new WorkoutExercise();
      workoutExercise.setExercise(workoutExerciseIn.getExercise());
      workoutExercise.setWorkout(workoutSession);
      workoutExercise.setPositionIndex(workoutExerciseIn.getPositionIndex());
      workoutExercise.setPerformStatus(EPerformStatus.PERFORMED);
      workoutExercise.setCreatedBy(createdBy);
      workoutExercise.setNumberOfWarmUpSets(workoutExerciseIn.getNumberOfWarmUpSets());

      List<WorkoutSet> workoutSets = new ArrayList<>();
      for (WorkoutSet workoutSetIn : workoutExerciseIn.getWorkoutSets()) {
        WorkoutSet workoutSet = new WorkoutSet();
        workoutSet.setWeight(workoutSetIn.getWeight());
        workoutSet.setCreatedBy(createdBy);
        workoutSet.setRestTime(workoutSetIn.getRestTime());
        workoutSet.setRepsCount(workoutSetIn.getRepsCount());
        workoutSet.setIsPerformed(true);
        workoutSet.setPositionIndex(workoutSetIn.getPositionIndex());

        workoutSets.add(workoutSet);
      }
      workoutExercise.setWorkoutSets(workoutSets);

      workoutExercises.add(workoutExercise);
    }
    workoutSession.setWorkoutExerciseList(workoutExercises);

    return workoutSessionRepository.save(workoutSession);
  }
}
