package com.blinnproject.myworkdayback.service.training;

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
  public Optional<Training> findById(Long id) {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return trainingRepository.findByIdAndCreatedBy(id, userDetails.getId());
  }

  @Transactional
  public List<TrainingExercises> validateTraining(Long trainingId, Date trainingDate) {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    // Check if training with the same performed date already exists
    if (trainingRepository.existsByParentIdAndPerformedDateAndTrainingStatusAndCreatedBy(trainingId, trainingDate, ETrainingStatus.PERFORMED, userDetails.getId())) {
      throw new TrainingAlreadyPerformedException("Training with id " + trainingId + " and performed date " + trainingDate + " already exists");
    }

    Training training = this.trainingRepository.findByIdAndCreatedBy(trainingId, userDetails.getId()).orElseThrow(() -> new TrainingWithCurrentUserNotFound("Training with id " + trainingId + " does not belong to current user"));

    EDayOfWeek providedDayDate = EDayOfWeek.of(Integer.parseInt(new SimpleDateFormat("u").format(trainingDate)));
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
  public List<TrainingExercises> modifyAndValidateTraining(Long trainingId, Date trainingDate, ModifyAndValidateRequest requestBody) {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    // Check if training with the same performed date already exists
    if (trainingRepository.existsByParentIdAndPerformedDateAndTrainingStatusAndCreatedBy(trainingId, trainingDate, ETrainingStatus.PERFORMED, userDetails.getId())) {
      throw new TrainingAlreadyPerformedException("Training with id " + trainingId + " and performed date " + trainingDate + " already exists");
    }

    Training originalTraining = this.trainingRepository.findByIdAndCreatedBy(trainingId, userDetails.getId()).orElseThrow(() -> new TrainingWithCurrentUserNotFound("Training with id " + trainingId + " does not belong to current user"));

    EDayOfWeek providedDayDate = EDayOfWeek.of(Integer.parseInt(new SimpleDateFormat("u").format(trainingDate)));
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

  @Transactional
  public TrainingExercises addExercise(Long trainingId, Long exerciseId, TrainingExercisesCreateDTO trainingExercisesCreateDTO) {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Training training = this.trainingRepository.findByIdAndCreatedBy(trainingId, userDetails.getId()).orElseThrow(() -> new TrainingWithCurrentUserNotFound("Training with id " + trainingId + " does not belong to current user"));
    Exercise exercise = this.exerciseService.findById(exerciseId).orElseThrow(() -> new ResourceNotFoundException("Exercise", "id", exerciseId));

    TrainingExercises trainingExercises = new TrainingExercises(training, exercise, trainingExercisesCreateDTO);

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

  @Transactional
  public List<FormattedTrainingData> formatTrainingExercisesSeriesInfo(List<TrainingExercisesSeriesInfo> input, Date trainingDate) {
    Map<Long, FormattedTrainingData> trainingMap = new HashMap<>();

    for (TrainingExercisesSeriesInfo seriesInfo : input) {
      EDayOfWeek providedDayDate = EDayOfWeek.of(Integer.parseInt(new SimpleDateFormat("u").format(trainingDate)));
      if (!seriesInfo.getTrainingDays().contains(providedDayDate)) {
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

    // Check and update exerciseState after processing series for each exercise
    for (FormattedTrainingData trainingData : trainingMap.values()) {
      for (FormattedTrainingData.ExerciseData exerciseData : trainingData.getTrainingExercises()) {
        ExerciseState exerciseState = ExerciseState.NOT_STARTED;

        for (FormattedTrainingData.ExerciseData.SeriesEntry seriesEntry : exerciseData.getSeries()) {
          if (seriesEntry.isCompleted()) {
            exerciseState = ExerciseState.STARTED;
            break;
          }
        }

        if (exerciseState == ExerciseState.STARTED) {
          boolean allSeriesCompleted = exerciseData.getSeries().stream().allMatch(FormattedTrainingData.ExerciseData.SeriesEntry::isCompleted);
          if (allSeriesCompleted) {
            exerciseState = ExerciseState.COMPLETED;
          }
        }

        exerciseData.setExerciseState(exerciseState);
      }
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

  @Transactional
  public void cancelTrainingDay(Long trainingParentId, Date trainingDayDate) {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Training parentTraining = this.trainingRepository.findByIdAndCreatedBy(trainingParentId, userDetails.getId()).orElseThrow(() -> new TrainingWithCurrentUserNotFound("Training with id " + trainingParentId + " does not belong to current user"));

    Training training = trainingRepository.findByParentIdAndPerformedDateAndCreatedBy(trainingParentId, trainingDayDate, userDetails.getId()).orElse(null);

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
  public void resetTrainingDay(Long trainingParentId, Date trainingDayDate) {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (this.trainingRepository.existsByParentIdAndPerformedDateAndCreatedBy(trainingParentId, trainingDayDate, userDetails.getId())) {
      this.trainingRepository.deleteByParentIdAndCreatedByAndPerformedDate(trainingParentId, userDetails.getId(), trainingDayDate);
    }
  }

  @Transactional(readOnly = true)
  public List<TrainingCalendarDTO> getTrainingCalendarInfo(Date startDate, Date endDate) throws Exception {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    List<Object[]> transformedData = this.trainingRepository.getTrainingCalendarData(userDetails.getId(), startDate, endDate);

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
