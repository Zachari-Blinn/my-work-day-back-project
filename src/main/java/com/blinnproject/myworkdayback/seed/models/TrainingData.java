package com.blinnproject.myworkdayback.seed.models;

import com.blinnproject.myworkdayback.model.Training;
import com.blinnproject.myworkdayback.model.User;
import com.blinnproject.myworkdayback.repository.TrainingRepository;
import com.blinnproject.myworkdayback.seed.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainingData {

  private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

  @Autowired
  TrainingRepository trainingRepository;

  @Autowired
  UserData userData;

  public void load() throws ChangeSetPersister.NotFoundException {
    if (trainingRepository.count() == 0) {
      logger.info("Seeding training...");

      User user1 = this.userData.findUserByUsername("jean-sebastien");

      Training hautDuCorpsTraining = new Training();
      hautDuCorpsTraining.setName("Haut du corps");
      hautDuCorpsTraining.setCreatedBy(user1.getId());
      // Add more exercise here

      trainingRepository.save(hautDuCorpsTraining);
      logger.info(String.valueOf(trainingRepository.count()) + " exercise successfully loaded!");
    }
  }
}
