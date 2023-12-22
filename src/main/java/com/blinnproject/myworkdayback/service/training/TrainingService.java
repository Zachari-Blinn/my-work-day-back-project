package com.blinnproject.myworkdayback.service.training;

import com.blinnproject.myworkdayback.model.Training;
import com.blinnproject.myworkdayback.model.TrainingExercises;
import com.blinnproject.myworkdayback.payload.request.AddExerciseRequest;
import com.blinnproject.myworkdayback.payload.request.CreateTrainingRequest;
import com.blinnproject.myworkdayback.payload.request.ModifyBeforeValidateRequest;
import com.blinnproject.myworkdayback.payload.request.ValidateTrainingRequest;
import com.blinnproject.myworkdayback.payload.response.FormattedTrainingData;
import com.blinnproject.myworkdayback.payload.response.TrainingExercisesSeriesInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TrainingService {

  Training create(CreateTrainingRequest training);

  List<Training> getAllTrainingsByCreatedBy(Long createdBy);

  Optional<Training> findById(Long id);

  List<TrainingExercises> validateTrainingExercises(Long trainingId, ValidateTrainingRequest requestBody) throws Exception;

  TrainingExercises addExercise(Long trainingId, AddExerciseRequest requestBody);

  List<TrainingExercises> getExercisesByTrainingId(Long trainingId);

  List<TrainingExercises> getTemplateExercisesByTrainingId(Long trainingId);

  List<TrainingExercisesSeriesInfo> getSeriesStatus(Long trainingId, Date trainingDay);

  public List<FormattedTrainingData> formatTrainingExercisesSeriesInfo(List<TrainingExercisesSeriesInfo> input);

  List<TrainingExercises> modifyBeforeValidate(Long trainingId, ModifyBeforeValidateRequest requestBody);
}
