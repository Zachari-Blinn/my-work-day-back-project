package com.blinnproject.myworkdayback.service.training;

import com.blinnproject.myworkdayback.model.Training;
import com.blinnproject.myworkdayback.payload.request.CreateTrainingRequest;
import com.blinnproject.myworkdayback.repository.TrainingRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TrainingServiceImpl implements TrainingService {

  @Autowired
  private TrainingRepository trainingRepository;

  @Override
  public Training create(Training training) {
    return trainingRepository.save(training);
  }
}
