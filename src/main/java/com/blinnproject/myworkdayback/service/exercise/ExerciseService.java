package com.blinnproject.myworkdayback.service.exercise;

import com.blinnproject.myworkdayback.model.Exercise;

import java.util.List;

public interface ExerciseService {
  Exercise create(Exercise exercise);

  List<Exercise> findAll();
}
