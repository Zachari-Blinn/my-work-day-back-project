package com.blinnproject.myworkdayback.service.training;

import com.blinnproject.myworkdayback.exception.*;
import com.blinnproject.myworkdayback.model.entity.Exercise;
import com.blinnproject.myworkdayback.model.entity.WorkoutExercise;
import com.blinnproject.myworkdayback.model.entity.WorkoutSession;
import com.blinnproject.myworkdayback.model.enums.ESessionStatus;
import com.blinnproject.myworkdayback.payload.dto.training.TrainingCreateDTO;
import com.blinnproject.myworkdayback.payload.dto.training_exercises.TrainingExercisesCreateDTO;
import com.blinnproject.myworkdayback.payload.query.TrainingCalendarDTO;
import com.blinnproject.myworkdayback.payload.request.ModifyAndValidateRequest;
import com.blinnproject.myworkdayback.payload.response.TrainingSessionInfoResponse;
import com.blinnproject.myworkdayback.repository.TrainingExercisesRepository;
import com.blinnproject.myworkdayback.repository.TrainingRepository;
import com.blinnproject.myworkdayback.service.exercise.ExerciseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  public WorkoutSession create(TrainingCreateDTO training) {
    return trainingRepository.save(new WorkoutSession(training));
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkoutSession> getAllTrainingsByCreatedBy(Long createdBy) {
    return trainingRepository.findAllByCreatedBy(createdBy);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<WorkoutSession> findById(Long id, Long createdBy) {
    return trainingRepository.findByIdAndCreatedBy(id, createdBy);
  }

  @Transactional
  public WorkoutExercise addExercise(Long trainingId, Long exerciseId, TrainingExercisesCreateDTO trainingExercisesCreateDTO, Long createdBy) {
    WorkoutSession training = getTrainingOrThrowError(trainingId, createdBy);
    Exercise exercise = this.exerciseService.findById(exerciseId).orElseThrow(() -> new ResourceNotFoundException("Exercise", "id", exerciseId));

    WorkoutExercise trainingExercises = new WorkoutExercise(training, exercise, trainingExercisesCreateDTO);

    return trainingExercisesRepository.save(trainingExercises);
  }

  @Transactional(readOnly = true)
  public List<WorkoutExercise> getExercisesByTrainingId(Long trainingId, Long createdBy) {
    return trainingExercisesRepository.findByTrainingIdAndTrainingCreatedBy(trainingId, createdBy);
  }

  @Transactional(readOnly = true)
  public List<WorkoutExercise> getTemplateExercisesByTrainingId(Long trainingId, Long createdBy) {
    return trainingExercisesRepository.findTemplateByTrainingIdAndCreatedBy(trainingId, createdBy);
  }

  @Transactional(readOnly = true)
  public List<TrainingCalendarDTO> getTrainingCalendarInfo(Date startDate, Date endDate, Long createdBy) throws Exception {
    List<Object[]> transformedData = this.trainingRepository.getTrainingCalendarData(createdBy, startDate, endDate);

    return this.formatTrainingCalendarData(transformedData);
  }

  @Transactional(readOnly = true)
  public List<TrainingSessionInfoResponse> getAllTrainingSessionsInfoCSV(Date startDate, Date endDate, Long createdBy) {
    return trainingRepository.findAllTrainingSession(createdBy, startDate, endDate);
  }

  public void patchTrainingSessionByParent(Long trainingParentId, Date trainingDate, ModifyAndValidateRequest requestBody, Long createdBy) {
    // check if trainingParentId exist, belong to current user and is a template
    if (!verifyTrainingSessionTemplate(trainingParentId, createdBy)) {
      throw new TrainingSessionTemplateNotFoundException("Training session with id " + trainingParentId + " does not exist or does not belong to current user or is not a template");
    }

    // check if training session already exists, if not create it, if yes, update it
    if (verifyIfTrainingSessionAlreadyPerformed(trainingParentId, trainingDate, createdBy)) {
      // todo modify training session
    } else {
      // todo create training session
    }
  }

  /**
   * PRIVATE METHODS
   */

  private boolean verifyTrainingSessionTemplate(Long trainingParentId, Long createdBy) {
    return trainingRepository.trainingSessionTemplateExists(trainingParentId, createdBy);
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

  private boolean verifyIfTrainingSessionAlreadyPerformed(Long trainingId, Date trainingDate, Long createdBy) {
    return trainingRepository.existsByParentIdAndPerformedDateAndTrainingStatusAndCreatedBy(trainingId, trainingDate, ESessionStatus.PERFORMED, createdBy);
  }

  private WorkoutSession getTrainingOrThrowError(Long trainingId, Long createdBy) {
    return trainingRepository.findByIdAndCreatedBy(trainingId, createdBy)
      .orElseThrow(() -> new TrainingWithCurrentUserNotFound("Training with id " + trainingId + " does not belong to current user"));
  }
}
