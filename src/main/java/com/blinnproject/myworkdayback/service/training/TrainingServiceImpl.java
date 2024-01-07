package com.blinnproject.myworkdayback.service.training;

import com.blinnproject.myworkdayback.exception.InvalidDayOfWeekProvidedForTraining;
import com.blinnproject.myworkdayback.exception.ResourceNotFoundException;
import com.blinnproject.myworkdayback.exception.TrainingAlreadyPerformedException;
import com.blinnproject.myworkdayback.exception.TrainingWithCurrentUserNotFound;
import com.blinnproject.myworkdayback.model.*;
import com.blinnproject.myworkdayback.payload.dto.training.TrainingCreateDTO;
import com.blinnproject.myworkdayback.payload.dto.training_exercises.TrainingExercisesCreateDTO;
import com.blinnproject.myworkdayback.payload.query.TrainingCalendarDTO;
import com.blinnproject.myworkdayback.payload.request.ModifyAndValidateRequest;
import com.blinnproject.myworkdayback.payload.response.ExerciseState;
import com.blinnproject.myworkdayback.payload.response.FormattedTrainingData;
import com.blinnproject.myworkdayback.payload.projection.TrainingExercisesSeriesInfoProjection;
import com.blinnproject.myworkdayback.repository.TrainingExercisesRepository;
import com.blinnproject.myworkdayback.repository.TrainingRepository;
import com.blinnproject.myworkdayback.service.exercise.ExerciseService;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.blinnproject.myworkdayback.util.FormatUtil.convertJsonToList;

@Service
public class TrainingServiceImpl implements TrainingService {

  private final TrainingRepository trainingRepository;
  private final TrainingExercisesRepository trainingExercisesRepository;
  private final ExerciseService exerciseService;

  public TrainingServiceImpl(TrainingRepository trainingRepository, TrainingExercisesRepository trainingExercisesRepository, ExerciseService exerciseService) {
    this.trainingRepository = trainingRepository;
    this.trainingExercisesRepository = trainingExercisesRepository;
    this.exerciseService = exerciseService;
  }

  @Override
  @Transactional
  public Training create(TrainingCreateDTO training) {
    return trainingRepository.save(new Training(training));
  }

  @Override
  @Transactional(readOnly = true)
  public List<Training> getAllTrainingsByCreatedBy(Long createdBy) {
    return trainingRepository.findAllByCreatedBy(createdBy);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Training> findById(Long id, Long createdBy) {
    return trainingRepository.findByIdAndCreatedBy(id, createdBy);
  }

  @Transactional
  public List<TrainingExercises> validateTraining(Long trainingId, Date trainingDate, Long createdBy) {
    // Check if training with the same performed date already exists
    checkIfTrainingAlreadyPerformed(trainingId, trainingDate, createdBy);

    Training training = this.trainingRepository.findByIdAndCreatedBy(trainingId, createdBy).orElseThrow(() -> new TrainingWithCurrentUserNotFound("Training with id " + trainingId + " does not belong to current user"));

    EDayOfWeek providedDayDate = EDayOfWeek.of(Integer.parseInt(new SimpleDateFormat("u").format(trainingDate)));

    if (!training.getTrainingDays().contains(providedDayDate)) {
      throw new InvalidDayOfWeekProvidedForTraining("The day: " + providedDayDate + " is not in training days list: " + training.getTrainingDays());
    }

    List<TrainingExercises> trainingExercises = trainingExercisesRepository.findTemplateByTrainingIdAndCreatedBy(trainingId, training.getCreatedBy());

    Training clonedTraining = new Training(training);
    clonedTraining.setTrainingStatus(ETrainingStatus.PERFORMED);
    clonedTraining.setPerformedDate(trainingDate);
    Training newTraining = trainingRepository.save(clonedTraining);

    List<TrainingExercises> clonedExercises = new ArrayList<>();
    for (TrainingExercises original : trainingExercises) {
      TrainingExercises cloned = new TrainingExercises(original);
      cloned.setTraining(newTraining);
      cloned.setTrainingDay(trainingDate);
      clonedExercises.add(cloned);
    }

    return trainingExercisesRepository.saveAll(clonedExercises);
  }

  private void checkIfTrainingAlreadyPerformed(Long trainingId, Date trainingDate, Long createdBy) {
    if (trainingRepository.existsByParentIdAndPerformedDateAndTrainingStatusAndCreatedBy(trainingId, trainingDate, ETrainingStatus.PERFORMED, createdBy)) {
      throw new TrainingAlreadyPerformedException("Training with id " + trainingId + " and performed date " + trainingDate + " already exists");
    }
  }

  @Transactional
  public List<TrainingExercises> modifyAndValidateTraining(Long trainingId, Date trainingDate, ModifyAndValidateRequest requestBody, Long createdBy) {
    // Check if training with the same performed date already exists
    checkIfTrainingAlreadyPerformed(trainingId, trainingDate, createdBy);

    Training originalTraining = this.trainingRepository.findByIdAndCreatedBy(trainingId, createdBy).orElseThrow(() -> new TrainingWithCurrentUserNotFound("Training with id " + trainingId + " does not belong to current user"));

    EDayOfWeek providedDayDate = EDayOfWeek.of(Integer.parseInt(new SimpleDateFormat("u").format(trainingDate)));

    if (!originalTraining.getTrainingDays().contains(providedDayDate)) {
      throw new InvalidDayOfWeekProvidedForTraining("The day: " + providedDayDate + " is not in training days list: " + originalTraining.getTrainingDays());
    }

    // Clone the training and set status to performed
    Training clonedTraining = new Training(originalTraining);
    clonedTraining.setTrainingStatus(ETrainingStatus.PERFORMED);
    clonedTraining.setPerformedDate(trainingDate);
    Training newTraining = trainingRepository.save(clonedTraining);

    List<TrainingExercises> clonedExercises = Arrays.asList(requestBody.getTrainingSession());
    for (TrainingExercises clonedExercise : clonedExercises) {
      clonedExercise.setTraining(newTraining); // check
      clonedExercise.setTrainingDay(trainingDate);
    }

    return trainingExercisesRepository.saveAll(clonedExercises);
  }

  @Transactional
  public TrainingExercises addExercise(Long trainingId, Long exerciseId, TrainingExercisesCreateDTO trainingExercisesCreateDTO, Long createdBy) {
    Training training = this.trainingRepository.findByIdAndCreatedBy(trainingId, createdBy).orElseThrow(() -> new TrainingWithCurrentUserNotFound("Training with id " + trainingId + " does not belong to current user"));
    Exercise exercise = this.exerciseService.findById(exerciseId).orElseThrow(() -> new ResourceNotFoundException("Exercise", "id", exerciseId));

    TrainingExercises trainingExercises = new TrainingExercises(training, exercise, trainingExercisesCreateDTO);

    return trainingExercisesRepository.save(trainingExercises);
  }

  @Transactional(readOnly = true)
  public List<TrainingExercises> getExercisesByTrainingId(Long trainingId, Long createdBy) {
    return trainingExercisesRepository.findByTrainingIdAndTrainingCreatedBy(trainingId, createdBy);
  }

  @Transactional(readOnly = true)
  public List<TrainingExercises> getTemplateExercisesByTrainingId(Long trainingId, Long createdBy) {
    return trainingExercisesRepository.findTemplateByTrainingIdAndCreatedBy(trainingId, createdBy);
  }

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

  @Transactional(readOnly = true)
  public List<FormattedTrainingData> getAllTrainingsSeriesStatusByDate(Date trainingDay, Long createdBy) {
    List<TrainingExercisesSeriesInfoProjection> trainingExercisesSeriesInfoList = trainingExercisesRepository.getAllTrainingsSeriesStatusByDate(createdBy, trainingDay);
    return formatTrainingExercisesSeriesInfo(trainingExercisesSeriesInfoList);
  }

  @Transactional
  public void cancelTrainingDay(Long trainingParentId, Date trainingDayDate, Long createdBy) {
    Training parentTraining = this.trainingRepository.findByIdAndCreatedBy(trainingParentId, createdBy).orElseThrow(() -> new TrainingWithCurrentUserNotFound("Training with id " + trainingParentId + " does not belong to current user"));

    Training training = trainingRepository.findByParentIdAndPerformedDateAndCreatedBy(trainingParentId, trainingDayDate, createdBy).orElse(null);

    if (training == null) {
      Training clonedTraining = new Training(parentTraining);
      clonedTraining.setTrainingStatus(ETrainingStatus.CANCELLED);
      clonedTraining.setPerformedDate(trainingDayDate);
      trainingRepository.save(clonedTraining);
    } else {
      training.setTrainingStatus(ETrainingStatus.CANCELLED);
      trainingRepository.save(training);
    }
  }

  @Transactional
  public void resetTrainingDay(Long trainingParentId, Date trainingDayDate, Long createdBy) {
    if (this.trainingRepository.existsByParentIdAndPerformedDateAndCreatedBy(trainingParentId, trainingDayDate, createdBy)) {
      this.trainingRepository.deleteByParentIdAndCreatedByAndPerformedDate(trainingParentId, createdBy, trainingDayDate);
    }
  }

  @Transactional(readOnly = true)
  public List<TrainingCalendarDTO> getTrainingCalendarInfo(Date startDate, Date endDate, Long createdBy) throws Exception {
    List<Object[]> transformedData = this.trainingRepository.getTrainingCalendarData(createdBy, startDate, endDate);

    return this.formatTrainingCalendarData(transformedData);
  }

  private List<TrainingCalendarDTO> formatTrainingCalendarData(List<Object[]> transformedData) throws Exception {
    List<TrainingCalendarDTO> formattedData = new ArrayList<>();

    for (Object[] data : transformedData) {
      TrainingCalendarDTO formattedItem = new TrainingCalendarDTO();
      formattedItem.setDate(String.valueOf(data[0]));
      formattedItem.setTrainings(convertJsonToList((String) data[1]));

      formattedData.add(formattedItem);
    }
    return formattedData;
  }
}
