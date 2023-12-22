package com.blinnproject.myworkdayback.service.training;

import com.blinnproject.myworkdayback.model.Series;
import com.blinnproject.myworkdayback.model.Training;
import com.blinnproject.myworkdayback.model.TrainingExercises;
import com.blinnproject.myworkdayback.payload.request.AddExerciseRequest;
import com.blinnproject.myworkdayback.payload.request.CreateTrainingRequest;
import com.blinnproject.myworkdayback.payload.request.ModifyBeforeValidateRequest;
import com.blinnproject.myworkdayback.payload.request.ValidateTrainingRequest;
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
import java.text.Format;
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
  @Transactional(readOnly = false)
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
    return trainingRepository.findById(id);
  }

  @Transactional(readOnly = false)
  public List<TrainingExercises> validateTrainingExercises(Long trainingId, Date trainingDay) throws Exception {
    // Authorization and checkup
    Training training = this.findById(trainingId).orElseThrow(() -> new Exception("Training not found with id " + trainingId));
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long createdBy = userDetails.getId();
    Assert.state(Objects.equals(training.getCreatedBy(), createdBy), "Training with id " + trainingId + " does not belong to current user");

    // Check if trainingDay day is included in training trainingDays and if not already set
    Format formatter = new SimpleDateFormat("u");
    DayOfWeek currentDay = DayOfWeek.of(Integer.parseInt(formatter.format(trainingDay)));
    Assert.state(training.getTrainingDays().contains(currentDay), "The day: " + currentDay + " is not in training days list: " + training.getTrainingDays());

    List<TrainingExercises> trainingExercises = trainingExercisesRepository.findTemplateByTrainingId(trainingId);

    List<TrainingExercises> clonedExercises = new ArrayList<>();
    for (TrainingExercises original : trainingExercises) {
      TrainingExercises cloned = new TrainingExercises(original);
      cloned.setTrainingDay(trainingDay);
      clonedExercises.add(cloned);
    }

    return trainingExercisesRepository.saveAll(clonedExercises);
  }

  @Override
  public List<TrainingExercises> modifyBeforeValidate(Long trainingId, ModifyBeforeValidateRequest requestBody) {
    List<TrainingExercises> clonedExercises = Arrays.asList(requestBody.getTrainingSession());
    return trainingExercisesRepository.saveAll(clonedExercises);
  }

  @Transactional(readOnly = false)
  public TrainingExercises addExercise(Long trainingId, AddExerciseRequest requestBody) {
    TrainingExercises trainingExercises = new TrainingExercises();

    trainingExercises.setTraining(this.findById(trainingId).orElse(null));
    trainingExercises.setExercise(this.exerciseService.findById(requestBody.getExerciseId()).orElse(null));
    trainingExercises.setNotes(requestBody.getNotes());
    trainingExercises.setNumberOfWarmUpSeries(requestBody.getNumberOfWarmUpSeries());

    List<Series> seriesList = requestBody.getSeries();
    trainingExercises.addSeriesList(seriesList);

    return trainingExercisesRepository.save(trainingExercises);
  }

  @Transactional(readOnly = true)
  public List<TrainingExercises> getExercisesByTrainingId(Long trainingId) {
      return trainingExercisesRepository.findByTrainingId(trainingId);
  }

  @Transactional(readOnly = true)
  public List<TrainingExercises> getTemplateExercisesByTrainingId(Long trainingId) {
    return trainingExercisesRepository.findTemplateByTrainingId(trainingId);
  }

  @Transactional(readOnly = true)
  public List<TrainingExercisesSeriesInfo> getSeriesStatus(Long trainingId, Date trainingDay) {
    return trainingExercisesRepository.checkIfTrainingExercisesSeriesIsCompleted(trainingId, trainingDay);
  }

  public List<FormattedTrainingData> formatTrainingExercisesSeriesInfo(List<TrainingExercisesSeriesInfo> input) {
    Map<Long, FormattedTrainingData> trainingMap = new HashMap<>();

    for (TrainingExercisesSeriesInfo seriesInfo : input) {
      long trainingId = seriesInfo.getTrainingId();

      trainingMap.computeIfAbsent(trainingId, k -> {
        FormattedTrainingData trainingData = new FormattedTrainingData();
        trainingData.setTrainingId(seriesInfo.getTrainingId());
        trainingData.setTrainingName(seriesInfo.getTrainingName());
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
}
