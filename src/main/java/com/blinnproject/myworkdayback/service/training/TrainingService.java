package com.blinnproject.myworkdayback.service.training;

import com.blinnproject.myworkdayback.model.Training;
import com.blinnproject.myworkdayback.payload.request.CreateTrainingRequest;

public interface TrainingService {

  Training create(Training training);

}
