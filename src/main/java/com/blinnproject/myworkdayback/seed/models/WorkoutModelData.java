package com.blinnproject.myworkdayback.seed.models;

import com.blinnproject.myworkdayback.model.dto.WorkoutModelCreateDTO;
import com.blinnproject.myworkdayback.model.entity.User;
import com.blinnproject.myworkdayback.model.entity.WorkoutModel;
import com.blinnproject.myworkdayback.model.enums.ESport;
import com.blinnproject.myworkdayback.repository.WorkoutModelRepository;
import com.blinnproject.myworkdayback.service.mapper.WorkoutModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Component;

@Component
public class WorkoutModelData {
  private static final Logger logger = LoggerFactory.getLogger(TrainingData.class);
  private final WorkoutModelRepository workoutModelRepository;

  private final WorkoutModelMapper workoutModelMapper;

  private final UserData userData;

  public User user;

  public WorkoutModelData(WorkoutModelRepository workoutModelRepository, UserData userData, WorkoutModelMapper workoutModelMapper) {
    this.workoutModelRepository = workoutModelRepository;
    this.userData = userData;
    this.workoutModelMapper = workoutModelMapper;
  }

  public void load() throws ChangeSetPersister.NotFoundException {
    if (workoutModelRepository.count() == 0) {
      logger.info("Seeding workout model...");

      this.user = this.userData.findUserByUsername("jean-sebastien");

      this.loadWorkoutModel();

      logger.info("{} workout model successfully loaded!", workoutModelRepository.count());
    }
  }

  public void loadWorkoutModel() {
    // Add more workout model here
    WorkoutModel workoutModel = new WorkoutModel(
        "MMA",
        "Mixed Martial Arts",
        String.valueOf(ESport.MMA),
        true,
        true,
        "icon_dumbbell",
        "#0072db"
    );

    this.workoutModelRepository.save(workoutModel);
  }
}
