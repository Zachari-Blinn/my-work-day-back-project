package com.blinnproject.myworkdayback.service.training;

import com.blinnproject.myworkdayback.model.entity.TrainingExercises;
import com.blinnproject.myworkdayback.model.entity.Training;
import com.blinnproject.myworkdayback.payload.dto.training.TrainingCreateDTO;
import com.blinnproject.myworkdayback.payload.dto.training_exercises.TrainingExercisesCreateDTO;
import com.blinnproject.myworkdayback.payload.query.TrainingCalendarDTO;
import com.blinnproject.myworkdayback.payload.request.ModifyAndValidateRequest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainingService {

  Training create(TrainingCreateDTO trainingDTO);

  List<Training> getAllTrainingsByCreatedBy(Long createdBy);

  Optional<Training> findById(Long id, Long createdBy);

  TrainingExercises addExercise(Long trainingId, Long exerciseId, TrainingExercisesCreateDTO trainingExercisesCreateDTO, Long createdBy);

  List<TrainingExercises> getExercisesByTrainingId(Long trainingId, Long createdBy);

  List<TrainingExercises> getTemplateExercisesByTrainingId(Long trainingId, Long createdBy);





  List<TrainingCalendarDTO> getTrainingCalendarInfo(Date startDate, Date endDate, Long createdBy) throws Exception;

  List<?> getAllTrainingSessionsInfoCSV(Date startDate, Date endDate, Long createdBy);

  void patchTrainingSessionByParent(Long trainingParentId, Date trainingDate, ModifyAndValidateRequest requestBody, Long createdBy);
}
