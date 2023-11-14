package com.blinnproject.myworkdayback.service.training;

import com.blinnproject.myworkdayback.model.Training;
import com.blinnproject.myworkdayback.payload.request.CreateTrainingRequest;

import java.util.List;
import java.util.Optional;

public interface TrainingService {

  Training create(Long userId, Training training);

  List<Training> getAllTrainingsByUserId(Long userId);

  Optional<Training> findById(Long id);

}
