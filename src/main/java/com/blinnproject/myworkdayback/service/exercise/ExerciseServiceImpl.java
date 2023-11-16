package com.blinnproject.myworkdayback.service.exercise;

import com.blinnproject.myworkdayback.model.Exercise;
import com.blinnproject.myworkdayback.repository.ExerciseRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

  @Autowired
  private final ExerciseRepository exerciseRepository;
  @Override
  public Exercise create(Exercise exercise) {
    return exerciseRepository.save(exercise);
  }

  @Override
  public List<Exercise> findAll() {
    return exerciseRepository.findAll();
  }
}
