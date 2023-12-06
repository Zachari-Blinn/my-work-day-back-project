package com.blinnproject.myworkdayback.service.training;

import com.blinnproject.myworkdayback.model.Training;
import com.blinnproject.myworkdayback.model.TrainingExercises;
import com.blinnproject.myworkdayback.payload.request.AddExerciseRequest;
import com.blinnproject.myworkdayback.payload.request.ValidateTrainingRequest;
import java.util.List;
import java.util.Optional;

public interface TrainingService {

  Training create(Training training);

  List<Training> getAllTrainingsByCreatedBy(Long createdBy);

  Optional<Training> findById(Long id);

  List<TrainingExercises> validateTrainingExercises(Long trainingId, ValidateTrainingRequest requestBody) throws Exception;

  TrainingExercises addExercise(Long trainingId, AddExerciseRequest requestBody);

  List<TrainingExercises> getExercisesByTrainingId(Long trainingId);

  List<TrainingExercises> getTemplateExercisesByTrainingId(Long trainingId);
}
