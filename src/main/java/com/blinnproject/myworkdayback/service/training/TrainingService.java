package com.blinnproject.myworkdayback.service.training;

import com.blinnproject.myworkdayback.model.Training;
import com.blinnproject.myworkdayback.model.TrainingExercises;
import com.blinnproject.myworkdayback.payload.request.AddExerciseRequest;
import com.blinnproject.myworkdayback.payload.request.CreateTrainingRequest;
import com.blinnproject.myworkdayback.payload.request.ModifyBeforeValidateRequest;
import com.blinnproject.myworkdayback.payload.response.FormattedTrainingData;
import com.blinnproject.myworkdayback.payload.response.TrainingExercisesSeriesInfo;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainingService {

  Training create(CreateTrainingRequest training);

  List<Training> getAllTrainingsByCreatedBy(Long createdBy);

  Optional<Training> findById(Long id);

  List<TrainingExercises> validateTrainingExercises(Long trainingId, Date trainingDay);

  TrainingExercises addExercise(Long trainingId, AddExerciseRequest requestBody);

  List<TrainingExercises> getExercisesByTrainingId(Long trainingId);

  List<TrainingExercises> getTemplateExercisesByTrainingId(Long trainingId);

  List<FormattedTrainingData> formatTrainingExercisesSeriesInfo(List<TrainingExercisesSeriesInfo> input, Date trainingDate);

  List<TrainingExercises> modifyBeforeValidate(Long trainingId, Date trainingDate, ModifyBeforeValidateRequest requestBody);

  // get training session info by date
  List<TrainingExercisesSeriesInfo> getTrainingSeriesStatusByDate(Long trainingId, Date trainingDay);
  List<TrainingExercisesSeriesInfo> getAllTrainingsSeriesStatusByDate(Date trainingDay);
}
