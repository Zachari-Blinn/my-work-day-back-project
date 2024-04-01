package com.blinnproject.myworkdayback.seed;

import com.blinnproject.myworkdayback.seed.models.ExerciseData;
import com.blinnproject.myworkdayback.seed.models.UserData;
import com.blinnproject.myworkdayback.seed.models.WorkoutModelData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

  private final UserData userData;

  private final ExerciseData exerciseData;

  private final WorkoutModelData workoutModelData;

  public DataLoader(UserData userData, ExerciseData exerciseData, WorkoutModelData workoutModelData) {
    this.userData = userData;
    this.exerciseData = exerciseData;
    this.workoutModelData = workoutModelData;
  }

  @Override
  public void run(String... args) throws Exception {
    logger.info("Seeding database in progress...");

    userData.load();
    exerciseData.load();
    workoutModelData.load();
  }
}
