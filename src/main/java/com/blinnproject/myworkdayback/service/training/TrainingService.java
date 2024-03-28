package com.blinnproject.myworkdayback.service.training;

import com.blinnproject.myworkdayback.model.entity.WorkoutExercise;
import com.blinnproject.myworkdayback.model.entity.WorkoutSession;
import com.blinnproject.myworkdayback.payload.dto.training.TrainingCreateDTO;
import com.blinnproject.myworkdayback.payload.dto.training_exercises.TrainingExercisesCreateDTO;
import com.blinnproject.myworkdayback.payload.query.TrainingCalendarDTO;
import com.blinnproject.myworkdayback.payload.request.ModifyAndValidateRequest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainingService {

  WorkoutSession create(TrainingCreateDTO trainingDTO);

  List<WorkoutSession> getAllTrainingsByCreatedBy(Long createdBy);

  Optional<WorkoutSession> findById(Long id, Long createdBy);

  WorkoutExercise addExercise(Long trainingId, Long exerciseId, TrainingExercisesCreateDTO trainingExercisesCreateDTO, Long createdBy);

  List<WorkoutExercise> getExercisesByTrainingId(Long trainingId, Long createdBy);

  List<WorkoutExercise> getTemplateExercisesByTrainingId(Long trainingId, Long createdBy);





  List<TrainingCalendarDTO> getTrainingCalendarInfo(Date startDate, Date endDate, Long createdBy) throws Exception;

  List<?> getAllTrainingSessionsInfoCSV(Date startDate, Date endDate, Long createdBy);

  void patchTrainingSessionByParent(Long trainingParentId, Date trainingDate, ModifyAndValidateRequest requestBody, Long createdBy);
}
