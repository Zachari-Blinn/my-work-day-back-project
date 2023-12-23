package com.blinnproject.myworkdayback.service.training;

import com.blinnproject.myworkdayback.exception.ResourceNotFoundException;
import com.blinnproject.myworkdayback.exception.TrainingAlreadyPerformedException;
import com.blinnproject.myworkdayback.exception.TrainingWithCurrentUserNotFound;
import com.blinnproject.myworkdayback.model.*;
import com.blinnproject.myworkdayback.payload.request.AddExerciseRequest;
import com.blinnproject.myworkdayback.payload.request.CreateTrainingRequest;
import com.blinnproject.myworkdayback.payload.request.ModifyBeforeValidateRequest;
import com.blinnproject.myworkdayback.payload.response.FormattedTrainingData;
import com.blinnproject.myworkdayback.payload.response.TrainingExercisesSeriesInfo;
import com.blinnproject.myworkdayback.repository.TrainingExercisesRepository;
import com.blinnproject.myworkdayback.repository.TrainingRepository;
import com.blinnproject.myworkdayback.security.UserDetailsImpl;
import com.blinnproject.myworkdayback.service.exercise.ExerciseService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.*;

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
  @Transactional()
  public Training create(CreateTrainingRequest training) {
    Training newTraining = new Training();

    newTraining.setName(training.getName());
    newTraining.setTrainingDays(training.getTrainingDays());
    newTraining.setSportPreset(training.getSportPreset());
    newTraining.setDescription(training.getDescription());
    newTraining.setHasWarpUp(training.getHasWarpUp());
    newTraining.setHasStretching(training.getHasStretching());

    return trainingRepository.save(newTraining);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Training> getAllTrainingsByCreatedBy(Long createdBy) {
    return trainingRepository.findAllByCreatedBy(createdBy);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Training> findById(Long id) {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return trainingRepository.findByIdAndCreatedBy(id, userDetails.getId());
  }

  @Transactional()
  public List<TrainingExercises> validateTrainingExercises(Long trainingId, Date trainingDate) {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    // Check if training with the same performed date already exists
    if (trainingRepository.existsByParentIdAndPerformedDateAndTrainingStatusAndCreatedBy(trainingId, trainingDate, ETrainingStatus.PERFORMED, userDetails.getId())) {
      throw new TrainingAlreadyPerformedException("Training with id " + trainingId + " and performed date " + trainingDate + " already exists");
    }

    Training training = this.trainingRepository.findByIdAndCreatedBy(trainingId, userDetails.getId()).orElseThrow(() -> new TrainingWithCurrentUserNotFound("Training with id " + trainingId + " does not belong to current user"));

    DayOfWeek providedDayDate = DayOfWeek.of(Integer.parseInt(new SimpleDateFormat("u").format(trainingDate)));
    Assert.state(training.getTrainingDays().contains(providedDayDate), "The day: " + providedDayDate + " is not in training days list: " + training.getTrainingDays());

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

  @Override
  public List<TrainingExercises> modifyBeforeValidate(Long trainingId, Date trainingDate, ModifyBeforeValidateRequest requestBody) {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    // Check if training with the same performed date already exists
    if (trainingRepository.existsByParentIdAndPerformedDateAndTrainingStatusAndCreatedBy(trainingId, trainingDate, ETrainingStatus.PERFORMED, userDetails.getId())) {
      throw new TrainingAlreadyPerformedException("Training with id " + trainingId + " and performed date " + trainingDate + " already exists");
    }

    Training originalTraining = this.trainingRepository.findByIdAndCreatedBy(trainingId, userDetails.getId()).orElseThrow(() -> new TrainingWithCurrentUserNotFound("Training with id " + trainingId + " does not belong to current user"));

    DayOfWeek providedDayDate = DayOfWeek.of(Integer.parseInt(new SimpleDateFormat("u").format(trainingDate)));
    Assert.state(originalTraining.getTrainingDays().contains(providedDayDate), "The day: " + providedDayDate + " is not in training days list: " + originalTraining.getTrainingDays());

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

  @Transactional()
  public TrainingExercises addExercise(Long trainingId, AddExerciseRequest requestBody) {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Training training = this.trainingRepository.findByIdAndCreatedBy(trainingId, userDetails.getId()).orElseThrow(() -> new TrainingWithCurrentUserNotFound("Training with id " + trainingId + " does not belong to current user"));
    Exercise exercise = this.exerciseService.findById(requestBody.getExerciseId()).orElseThrow(() -> new ResourceNotFoundException("Exercise", "id", requestBody.getExerciseId()));

    TrainingExercises trainingExercises = new TrainingExercises();

    trainingExercises.setTraining(training);
    trainingExercises.setExercise(exercise);
    trainingExercises.setNotes(requestBody.getNotes());
    trainingExercises.setNumberOfWarmUpSeries(requestBody.getNumberOfWarmUpSeries());

    List<Series> seriesList = requestBody.getSeries();
    trainingExercises.addSeriesList(seriesList);

    return trainingExercisesRepository.save(trainingExercises);
  }

  @Transactional(readOnly = true)
  public List<TrainingExercises> getExercisesByTrainingId(Long trainingId) {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return trainingExercisesRepository.findByTrainingIdAndTrainingCreatedBy(trainingId, userDetails.getId());
  }

  @Transactional(readOnly = true)
  public List<TrainingExercises> getTemplateExercisesByTrainingId(Long trainingId) {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return trainingExercisesRepository.findTemplateByTrainingIdAndCreatedBy(trainingId, userDetails.getId());
  }

  public List<FormattedTrainingData> formatTrainingExercisesSeriesInfo(List<TrainingExercisesSeriesInfo> input, Date trainingDate) {
    Map<Long, FormattedTrainingData> trainingMap = new HashMap<>();

    for (TrainingExercisesSeriesInfo seriesInfo : input) {
      DayOfWeek currentDay = DayOfWeek.of(Integer.parseInt(new SimpleDateFormat("u").format(trainingDate)));
      if (!seriesInfo.getTrainingDays().contains(currentDay)) {
        continue;
      }
      long trainingId = seriesInfo.getTrainingId();

      trainingMap.computeIfAbsent(trainingId, k -> {
        FormattedTrainingData trainingData = new FormattedTrainingData();
        trainingData.setTrainingId(seriesInfo.getTrainingId());
        trainingData.setTrainingName(seriesInfo.getTrainingName());
        trainingData.setTrainingStatus(seriesInfo.getTrainingStatus());
        trainingData.setTrainingDays(seriesInfo.getTrainingDays());
        trainingData.setTrainingIconName(seriesInfo.getTrainingIconName());
        trainingData.setTrainingIconHexadecimalColor(seriesInfo.getTrainingIconHexadecimalColor());
        trainingData.setTrainingExercises(new ArrayList<>());
        trainingData.setNumberOfExercise(0);
        return trainingData;
      });

      FormattedTrainingData trainingData = trainingMap.get(trainingId);
      List<FormattedTrainingData.ExerciseData> trainingExercises = trainingData.getTrainingExercises();

      FormattedTrainingData.ExerciseData exerciseData = null;

      for (FormattedTrainingData.ExerciseData existingExercise : trainingExercises) {
        if (existingExercise.getExerciseId() == seriesInfo.getExerciseId()) {
          exerciseData = existingExercise;
          break;
        }
      }

      if (exerciseData == null) {
        exerciseData = new FormattedTrainingData.ExerciseData();
        exerciseData.setExerciseId(seriesInfo.getExerciseId());
        exerciseData.setExerciseName(seriesInfo.getExerciseName());
        exerciseData.setSeries(new ArrayList<>());

        trainingExercises.add(exerciseData);
        int numberOfExercise = trainingData.getNumberOfExercise();
        trainingData.setNumberOfExercise(numberOfExercise + 1);
      }

      List<FormattedTrainingData.ExerciseData.SeriesEntry> seriesList = exerciseData.getSeries();
      FormattedTrainingData.ExerciseData.SeriesEntry seriesEntry = new FormattedTrainingData.ExerciseData.SeriesEntry();
      seriesEntry.setId(seriesInfo.getSeriesId());
      seriesEntry.setPositionIndex(seriesInfo.getSeriesPositionIndex());
      seriesEntry.setRepsCount(seriesInfo.getSeriesRepsCount());
      seriesEntry.setRestTime(seriesInfo.getSeriesRestTime());
      seriesEntry.setWeight(seriesInfo.getSeriesWeight());
      seriesEntry.setCompleted(seriesInfo.isCompleted());

      seriesList.add(seriesEntry);
    }

    return new ArrayList<>(trainingMap.values());
  }

  @Transactional(readOnly = true)
  public List<TrainingExercisesSeriesInfo> getTrainingSeriesStatusByDate(Long trainingId, Date trainingDay) {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return trainingExercisesRepository.getTrainingSeriesStatusByDate(userDetails.getId(), trainingId, trainingDay);
  }

  @Transactional(readOnly = true)
  public List<TrainingExercisesSeriesInfo> getAllTrainingsSeriesStatusByDate(Date trainingDay) {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return trainingExercisesRepository.getAllTrainingsSeriesStatusByDate(userDetails.getId(), trainingDay);
  }
}
