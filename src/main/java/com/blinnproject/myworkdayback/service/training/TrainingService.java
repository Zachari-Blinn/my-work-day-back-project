package com.blinnproject.myworkdayback.service.training;

import com.blinnproject.myworkdayback.model.Training;
import com.blinnproject.myworkdayback.model.TrainingExercises;
import com.blinnproject.myworkdayback.payload.dto.training.TrainingCreateDTO;
import com.blinnproject.myworkdayback.payload.dto.training_exercises.TrainingExercisesCreateDTO;
import com.blinnproject.myworkdayback.payload.query.TrainingCalendarDTO;
import com.blinnproject.myworkdayback.payload.request.ModifyAndValidateRequest;
import com.blinnproject.myworkdayback.payload.response.FormattedTrainingData;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainingService {

  Training create(TrainingCreateDTO trainingDTO);

  List<Training> getAllTrainingsByCreatedBy(Long createdBy);

  Optional<Training> findById(Long id, Long createdBy);

  List<TrainingExercises> validateTraining(Long trainingId, Date trainingDay, Long createdBy);

  TrainingExercises addExercise(Long trainingId, Long exerciseId, TrainingExercisesCreateDTO trainingExercisesCreateDTO, Long createdBy);

  List<TrainingExercises> getExercisesByTrainingId(Long trainingId, Long createdBy);

  List<TrainingExercises> getTemplateExercisesByTrainingId(Long trainingId, Long createdBy);

  List<TrainingExercises> modifyAndValidateTraining(Long trainingId, Date trainingDate, ModifyAndValidateRequest requestBody, Long createdBy);

  List<FormattedTrainingData> getAllTrainingsSeriesStatusByDate(Date trainingDay, Long createdBy);

  void cancelTrainingDay(Long trainingParentId, Date trainingDay, Long createdBy);

  void resetTrainingDay(Long trainingParentId, Date trainingDay, Long createdBy);

  List<TrainingCalendarDTO> getTrainingCalendarInfo(Date startDate, Date endDate, Long createdBy) throws Exception;

  List<?> getAllTrainingSessionsInfoCSV(Date startDate, Date endDate, Long createdBy);
}
