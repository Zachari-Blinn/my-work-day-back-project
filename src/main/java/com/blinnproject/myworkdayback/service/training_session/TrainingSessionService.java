package com.blinnproject.myworkdayback.service.training_session;

import com.blinnproject.myworkdayback.model.entity.WorkoutExercise;
import com.blinnproject.myworkdayback.payload.request.ModifyAndValidateRequest;
import com.blinnproject.myworkdayback.payload.response.FormattedTrainingData;

import java.util.Date;
import java.util.List;

public interface TrainingSessionService {
  List<FormattedTrainingData> getAllTrainingsSeriesStatusByDate(Date trainingDay, Long createdBy);

  List<WorkoutExercise> validateTrainingSessionWithoutChange(Long trainingParentId, Date trainingDay, Long createdBy);

  List<WorkoutExercise> modifyDataAndValidateTrainingSession(Long trainingId, Date trainingDate, ModifyAndValidateRequest requestBody, Long createdBy);

  void resetTrainingSessionOfTheDay(Long trainingParentId, Date trainingDay, Long createdBy);

  void cancelTrainingSessionOfTheDay(Long trainingParentId, Date trainingDayDate, Long createdBy);
}
