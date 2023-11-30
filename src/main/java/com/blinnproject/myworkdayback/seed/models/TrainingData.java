package com.blinnproject.myworkdayback.seed.models;

import com.blinnproject.myworkdayback.model.*;
import com.blinnproject.myworkdayback.repository.SeriesRepository;
import com.blinnproject.myworkdayback.repository.TrainingExercisesRepository;
import com.blinnproject.myworkdayback.repository.TrainingRepository;
import com.blinnproject.myworkdayback.seed.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class TrainingData {

  private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

  @Autowired
  TrainingRepository trainingRepository;

  @Autowired
  TrainingExercisesRepository trainingExercisesRepository;

  @Autowired
  SeriesRepository seriesRepository;

  @Autowired
  UserData userData;

  @Autowired
  ExerciseData exerciseData;

  public User user;

  List<Training> trainingList = new ArrayList<Training>();
  List<TrainingExercises> trainingExercisesList = new ArrayList<TrainingExercises>();
  List<Series> seriesList = new ArrayList<>();

  public void load() throws ChangeSetPersister.NotFoundException {
    if (trainingRepository.count() == 0) {
      logger.info("Seeding training...");

      this.user = this.userData.findUserByUsername("jean-sebastien");

      this.loadExercisesAndCreateTraining();
      this.loadTrainingExercises();

      logger.info(String.valueOf(trainingRepository.count()) + " exercise successfully loaded!");
    }
  }

  private void loadExercisesAndCreateTraining() {
    Training hautDuCorpsTraining = new Training();
    hautDuCorpsTraining.setName("Haut du corps");
    hautDuCorpsTraining.setCreatedBy(this.user.getId());
    this.trainingList.add(hautDuCorpsTraining);
    // Add more exercise here

    trainingRepository.save(hautDuCorpsTraining);
  }

  public Training getTrainingByName(String name) throws ChangeSetPersister.NotFoundException {
    return this.trainingList.stream().filter(training -> training.getName().equals(name)).findFirst().orElseThrow(ChangeSetPersister.NotFoundException::new);
  }

  private void loadTrainingExercises() throws ChangeSetPersister.NotFoundException {
    Training training1 = this.getTrainingByName("Haut du corps");
    Exercise exercise1 = this.exerciseData.getExerciseByName("Bench press");

    TrainingExercisesKey trainingExercisesKey = new TrainingExercisesKey(training1.getId(), exercise1.getId());
    TrainingExercises trainingExercises = new TrainingExercises(trainingExercisesKey, "Lorem ipsum note", 2);

    trainingExercises.setTraining(training1);
    trainingExercises.setExercise(exercise1);

    this.loadSeries();

    trainingExercises.addSeriesList(this.seriesList);

    TrainingExercises createdTrainingExercises = this.trainingExercisesRepository.save(trainingExercises);
    this.trainingExercisesList.add(createdTrainingExercises);
  }

  private void loadSeries() {
    Series series1 = new Series();
    series1.setPositionIndex(1);
    series1.setRepsCount(8);
    series1.setRestTime("02:00");
    series1.setWeight(50);
    series1.setCreatedBy(this.user.getId());
    this.seriesList.add(series1);

    Series series2 = new Series();
    series2.setPositionIndex(2);
    series2.setRepsCount(10);
    series2.setRestTime("02:00");
    series2.setWeight(60);
    series2.setCreatedBy(this.user.getId());
    this.seriesList.add(series2);

    Series series3 = new Series();
    series3.setPositionIndex(2);
    series3.setRepsCount(10);
    series3.setRestTime("02:00");
    series3.setWeight(60);
    series3.setCreatedBy(this.user.getId());
    this.seriesList.add(series3);

    seriesRepository.saveAll(this.seriesList);
  }
}
