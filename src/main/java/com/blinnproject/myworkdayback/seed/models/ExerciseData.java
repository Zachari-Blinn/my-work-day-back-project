package com.blinnproject.myworkdayback.seed.models;

import com.blinnproject.myworkdayback.model.Exercise;
import com.blinnproject.myworkdayback.model.EMuscle;
import com.blinnproject.myworkdayback.model.User;
import com.blinnproject.myworkdayback.repository.ExerciseRepository;
import com.blinnproject.myworkdayback.seed.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
public class ExerciseData {

  private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

  @Autowired
  ExerciseRepository exerciseRepository;

  @Autowired
  UserData userData;

  public void load() throws ChangeSetPersister.NotFoundException {
    if (exerciseRepository.count() == 0) {
      logger.info("Seeding exercise...");

      User user1 = this.userData.findUserByUsername("jean-sebastien");

      Exercise benchPressExercise = new Exercise();
      benchPressExercise.setName("Bench press");
      benchPressExercise.setMusclesUsed(new HashSet<>(Arrays.asList(EMuscle.PECTORALIS_MAJOR, EMuscle.TRICEPS)));
      benchPressExercise.setCreatedBy(user1.getId());
      // Add more exercise here

      exerciseRepository.save(benchPressExercise);
      logger.info(String.valueOf(exerciseRepository.count()) + " exercise successfully loaded!");
    }
  }
}
