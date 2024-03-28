package com.blinnproject.myworkdayback.service.training_session;

import com.blinnproject.myworkdayback.exception.InvalidDayOfWeekProvidedForTraining;
import com.blinnproject.myworkdayback.exception.TrainingAlreadyPerformedException;
import com.blinnproject.myworkdayback.exception.TrainingWithCurrentUserNotFound;
import com.blinnproject.myworkdayback.model.entity.WorkoutExercise;
import com.blinnproject.myworkdayback.model.enums.EDayOfWeek;
import com.blinnproject.myworkdayback.model.enums.ESessionStatus;
import com.blinnproject.myworkdayback.model.entity.WorkoutSession;
import com.blinnproject.myworkdayback.payload.projection.TrainingExercisesSeriesInfoProjection;
import com.blinnproject.myworkdayback.payload.request.ModifyAndValidateRequest;
import com.blinnproject.myworkdayback.payload.response.ExerciseState;
import com.blinnproject.myworkdayback.payload.response.FormattedTrainingData;
import com.blinnproject.myworkdayback.repository.TrainingExercisesRepository;
import com.blinnproject.myworkdayback.repository.TrainingRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TrainingSessionServiceImpl implements TrainingSessionService {
  private final TrainingExercisesRepository trainingExercisesRepository;
  private final TrainingRepository trainingRepository;


  public TrainingSessionServiceImpl(TrainingExercisesRepository trainingExercisesRepository, TrainingRepository trainingRepository) {
    this.trainingExercisesRepository = trainingExercisesRepository;
    this.trainingRepository = trainingRepository;
  }

  @Transactional(readOnly = true)
  public List<FormattedTrainingData> getAllTrainingsSeriesStatusByDate(Date trainingDay, Long createdBy) {
    List<TrainingExercisesSeriesInfoProjection> trainingExercisesSeriesInfoList = trainingExercisesRepository.getAllTrainingsSeriesStatusByDate(createdBy, trainingDay);
    return formatTrainingExercisesSeriesInfo(trainingExercisesSeriesInfoList);
  }

  public List<WorkoutExercise> validateTrainingSessionWithoutChange(Long trainingParentId, Date trainingDate, Long createdBy) {
    if (verifyIfTrainingSessionAlreadyPerformed(trainingParentId, trainingDate, createdBy)) {
      throw new TrainingAlreadyPerformedException("Training with id " + trainingParentId + " and performed date " + trainingDate + " already exists");
    }

    WorkoutSession training = getTrainingOrThrowError(trainingParentId, createdBy);

    checkTrainingDayValidityOrThrowError(training, trainingDate);

    WorkoutSession newTraining = cloneTraining(training, trainingDate);
    List<WorkoutExercise> clonedExercises = cloneTrainingExercises(training, newTraining, trainingDate);

    return trainingExercisesRepository.saveAll(clonedExercises);
  }

  public List<WorkoutExercise> modifyDataAndValidateTrainingSession(Long trainingId, Date trainingDate, ModifyAndValidateRequest requestBody, Long createdBy) {
    if (verifyIfTrainingSessionAlreadyPerformed(trainingId, trainingDate, createdBy)) {
      throw new TrainingAlreadyPerformedException("Training with id " + trainingId + " and performed date " + trainingDate + " already exists");
    }

    WorkoutSession originalTraining = getTrainingOrThrowError(trainingId, createdBy);

    checkTrainingDayValidityOrThrowError(originalTraining, trainingDate);

    WorkoutSession newTraining = cloneTraining(originalTraining, trainingDate);
    List<WorkoutExercise> clonedExercises = updateClonedExercises(requestBody, newTraining, trainingDate);

    return trainingExercisesRepository.saveAll(clonedExercises);
  }

  @Transactional
  public void resetTrainingSessionOfTheDay(Long trainingParentId, Date trainingDayDate, Long createdBy) {
    if (this.trainingRepository.existsByParentIdAndPerformedDateAndCreatedBy(trainingParentId, trainingDayDate, createdBy)) {
      this.trainingRepository.deleteByParentIdAndCreatedByAndPerformedDate(trainingParentId, createdBy, trainingDayDate);
    }
  }

  @Transactional
  public void cancelTrainingSessionOfTheDay(Long trainingParentId, Date trainingDayDate, Long createdBy) {
    WorkoutSession parentTraining = getTrainingOrThrowError(trainingParentId, createdBy);

    WorkoutSession training = trainingRepository.findByParentIdAndPerformedDateAndCreatedBy(trainingParentId, trainingDayDate, createdBy).orElse(null);

    if (training == null) {
      WorkoutSession clonedTraining = new WorkoutSession(parentTraining);
      clonedTraining.setTrainingStatus(ESessionStatus.CANCELLED);
      clonedTraining.setPerformedDate(trainingDayDate);
      trainingRepository.save(clonedTraining);
    } else {
      training.setTrainingStatus(ESessionStatus.CANCELLED);
      trainingRepository.save(training);
    }
  }

  /*
   * Private methods
   */

  private List<FormattedTrainingData> formatTrainingExercisesSeriesInfo(@NotNull List<TrainingExercisesSeriesInfoProjection> projectionList) {
    Map<Long, FormattedTrainingData> trainingDataMap = new HashMap<>();

    for (TrainingExercisesSeriesInfoProjection projection : projectionList) {
      // create training template data
      long trainingId = projection.getTrainingId();
      trainingDataMap.computeIfAbsent(trainingId, k -> createNewFormattedTrainingData(projection));

      // add exercise data
      FormattedTrainingData trainingData = trainingDataMap.get(trainingId);
      FormattedTrainingData.ExerciseData exerciseData = getOrCreateExerciseData(trainingData, projection);

      // add series data
      FormattedTrainingData.ExerciseData.SeriesEntry seriesEntry = createSeriesEntry(projection);
      exerciseData.getSeries().add(seriesEntry);
    }

    // update exercise state
    updateExerciseState(trainingDataMap);

    return new ArrayList<>(trainingDataMap.values());
  }

  private FormattedTrainingData createNewFormattedTrainingData(@NotNull TrainingExercisesSeriesInfoProjection seriesInfo) {
    FormattedTrainingData trainingData = new FormattedTrainingData();

    trainingData.setTrainingId(seriesInfo.getTrainingId());
    trainingData.setTrainingName(seriesInfo.getTrainingName());
    trainingData.setTrainingDays(seriesInfo.getTrainingDays());
    trainingData.setTrainingStatus(seriesInfo.getTrainingStatus());
    trainingData.setTrainingIconName(seriesInfo.getTrainingIconName());
    trainingData.setTrainingIconHexadecimalColor(seriesInfo.getTrainingIconHexadecimalColor());
    trainingData.setTrainingExercises(new ArrayList<>());
    trainingData.setNumberOfExercise(0);

    return trainingData;
  }

  private FormattedTrainingData.ExerciseData getOrCreateExerciseData(@NotNull FormattedTrainingData trainingData, @NotNull TrainingExercisesSeriesInfoProjection seriesInfo) {
    return trainingData.getTrainingExercises()
      .stream()
      .filter(exercise -> exercise.getExerciseId() == seriesInfo.getExerciseId())
      .findFirst()
      .orElseGet(() -> {
        FormattedTrainingData.ExerciseData newExerciseData = new FormattedTrainingData.ExerciseData();

        newExerciseData.setExerciseId(seriesInfo.getExerciseId());
        newExerciseData.setExerciseName(seriesInfo.getExerciseName());
        newExerciseData.setExerciseState(ExerciseState.NOT_STARTED);
        newExerciseData.setSeries(new ArrayList<>());
        trainingData.getTrainingExercises().add(newExerciseData);
        int numberOfExercise = trainingData.getNumberOfExercise();
        trainingData.setNumberOfExercise(numberOfExercise + 1);

        return newExerciseData;
      });
  }

  private FormattedTrainingData.ExerciseData.SeriesEntry createSeriesEntry(@NotNull TrainingExercisesSeriesInfoProjection seriesInfo) {
    FormattedTrainingData.ExerciseData.SeriesEntry seriesEntry = new FormattedTrainingData.ExerciseData.SeriesEntry();

    seriesEntry.setId(seriesInfo.getSeriesId());
    seriesEntry.setPositionIndex(seriesInfo.getSeriesPositionIndex());
    seriesEntry.setRepsCount(seriesInfo.getSeriesRepsCount());
    seriesEntry.setRestTime(seriesInfo.getSeriesRestTime());
    seriesEntry.setWeight(seriesInfo.getSeriesWeight());
    seriesEntry.setCompleted(seriesInfo.getIsCompleted());

    return seriesEntry;
  }

  private void updateExerciseState(@NotNull Map<Long, @NotNull FormattedTrainingData> trainingDataMap) {
    for (FormattedTrainingData trainingData : trainingDataMap.values()) {
      for (FormattedTrainingData.ExerciseData exerciseData : trainingData.getTrainingExercises()) {
        ExerciseState exerciseState = calculateExerciseState(exerciseData);
        exerciseData.setExerciseState(exerciseState);
      }
    }
  }

  private ExerciseState calculateExerciseState(@NotNull FormattedTrainingData.ExerciseData exerciseData) {
    boolean seriesCompleted = exerciseData.getSeries().stream().anyMatch(FormattedTrainingData.ExerciseData.SeriesEntry::isCompleted);

    if (seriesCompleted) {
      boolean allSeriesCompleted = exerciseData.getSeries().stream().allMatch(FormattedTrainingData.ExerciseData.SeriesEntry::isCompleted);
      return allSeriesCompleted ? ExerciseState.COMPLETED : ExerciseState.STARTED;
    }

    return ExerciseState.NOT_STARTED;
  }

  private boolean verifyIfTrainingSessionAlreadyPerformed(Long trainingId, Date trainingDate, Long createdBy) {
    return trainingRepository.existsByParentIdAndPerformedDateAndTrainingStatusAndCreatedBy(trainingId, trainingDate, ESessionStatus.PERFORMED, createdBy);
  }

  private WorkoutSession getTrainingOrThrowError(Long trainingId, Long createdBy) {
    return trainingRepository.findByIdAndCreatedBy(trainingId, createdBy)
      .orElseThrow(() -> new TrainingWithCurrentUserNotFound("Training with id " + trainingId + " does not belong to current user"));
  }

  private void checkTrainingDayValidityOrThrowError(WorkoutSession training, Date trainingDate) {
    EDayOfWeek providedDayDate = EDayOfWeek.of(Integer.parseInt(new SimpleDateFormat("u").format(trainingDate)));

    if (!training.getTrainingDays().contains(providedDayDate)) {
      throw new InvalidDayOfWeekProvidedForTraining("The day: " + providedDayDate + " is not in training days list: " + training.getTrainingDays());
    }
  }

  private WorkoutSession cloneTraining(WorkoutSession originalTraining, Date trainingDate) {
    WorkoutSession clonedTraining = new WorkoutSession(originalTraining);
    clonedTraining.setTrainingStatus(ESessionStatus.PERFORMED);
    clonedTraining.setPerformedDate(trainingDate);
    return trainingRepository.save(clonedTraining);
  }

  private List<WorkoutExercise> cloneTrainingExercises(WorkoutSession originalTraining, WorkoutSession newTraining, Date trainingDate) {
    List<WorkoutExercise> trainingExercises = trainingExercisesRepository.findTemplateByTrainingIdAndCreatedBy(originalTraining.getId(), originalTraining.getCreatedBy());

    List<WorkoutExercise> clonedExercises = new ArrayList<>();
    for (WorkoutExercise original : trainingExercises) {
      WorkoutExercise cloned = new WorkoutExercise(original);
      cloned.setTraining(newTraining);
      cloned.setTrainingDay(trainingDate);
      clonedExercises.add(cloned);
    }
    return clonedExercises;
  }

  private List<WorkoutExercise> updateClonedExercises(ModifyAndValidateRequest requestBody, WorkoutSession newTraining, Date trainingDate) {
    List<WorkoutExercise> clonedExercises = Arrays.asList(requestBody.getTrainingSession());
    for (WorkoutExercise clonedExercise : clonedExercises) {
      clonedExercise.setTraining(newTraining);
      clonedExercise.setTrainingDay(trainingDate);
    }
    return clonedExercises;
  }
}
