package com.blinnproject.myworkdayback.seed.models;

import com.blinnproject.myworkdayback.model.Exercise;
import com.blinnproject.myworkdayback.model.Muscle;
import com.blinnproject.myworkdayback.repository.ExerciseRepository;
import com.blinnproject.myworkdayback.seed.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Component
public class ExerciseData {

  private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

  @Autowired
  ExerciseRepository exerciseRepository;

  public void load() {
    if (exerciseRepository.count() == 0) {
      logger.info("Seeding exercise...");

      Exercise benchPressExercise = new Exercise();
      benchPressExercise.setName("Bench press");
      benchPressExercise.setMusclesUsed(new HashSet<>(Arrays.asList(Muscle.PECTORALIS_MAJOR, Muscle.TRICEPS)));
      // Add more exercise here

      exerciseRepository.save(benchPressExercise);
      logger.info(String.valueOf(exerciseRepository.count()) + " exercise successfully loaded!");
    }
  }
}
