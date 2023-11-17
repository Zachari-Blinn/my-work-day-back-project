package com.blinnproject.myworkdayback.seed;

import com.blinnproject.myworkdayback.model.User;
import com.blinnproject.myworkdayback.repository.UserRepository;
import com.blinnproject.myworkdayback.seed.models.ExerciseData;
import com.blinnproject.myworkdayback.seed.models.TrainingData;
import com.blinnproject.myworkdayback.seed.models.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

  @Autowired
  UserData userData;

  @Autowired
  ExerciseData exerciseData;

  @Autowired
  TrainingData trainingData;

  @Override
  public void run(String... args) throws Exception {
    logger.info("Seeding database in progress...");

    userData.load();
    exerciseData.load();
    trainingData.load();
  }
}
