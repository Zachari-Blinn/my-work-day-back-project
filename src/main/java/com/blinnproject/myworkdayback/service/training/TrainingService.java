package com.blinnproject.myworkdayback.service.training;

import com.blinnproject.myworkdayback.model.Training;
import com.blinnproject.myworkdayback.payload.request.CreateTrainingRequest;

import java.util.List;
import java.util.Optional;

public interface TrainingService {

  Training create(Training training);

  List<Training> getAllTrainingsByCreatedBy(Long createdBy);

  Optional<Training> findById(Long id);

}
