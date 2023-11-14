package com.blinnproject.myworkdayback.service.training;

import com.blinnproject.myworkdayback.model.Training;
import com.blinnproject.myworkdayback.model.User;
import com.blinnproject.myworkdayback.payload.request.CreateTrainingRequest;
import com.blinnproject.myworkdayback.repository.TrainingRepository;
import com.blinnproject.myworkdayback.repository.UserRepository;
import io.swagger.v3.core.util.Json;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TrainingServiceImpl implements TrainingService {

  @Autowired
  private TrainingRepository trainingRepository;

  @Autowired
  private UserRepository userRepository;

  @Override
  public Training create(Long userId, Training training) {
    User user = userRepository.findById(userId).orElse(null);
    training.setUser(user);
    return trainingRepository.save(training);
  }

  @Override
  public List<Training> getAllTrainingsByUserId(Long userId) {
    return trainingRepository.findAllByUserId(userId);
  }

  @Override
  public Optional<Training> findById(Long id) {
    return trainingRepository.findById(id);
  }
}
