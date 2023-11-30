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

  List<Training> trainingList = new ArrayList<Training>();
  List<TrainingExercises> trainingExercisesList = new ArrayList<TrainingExercises>();
  List<Series> seriesList = new ArrayList<>();

  public void load() throws ChangeSetPersister.NotFoundException {
    if (trainingRepository.count() == 0) {
      logger.info("Seeding training...");

      this.loadExercisesAndCreateTraining();
      this.loadTrainingExercises();

      logger.info(String.valueOf(trainingRepository.count()) + " exercise successfully loaded!");
    }
  }

  private void loadExercisesAndCreateTraining() throws ChangeSetPersister.NotFoundException {
    User user1 = this.userData.findUserByUsername("jean-sebastien");

    Training hautDuCorpsTraining = new Training();
    hautDuCorpsTraining.setName("Haut du corps");
    hautDuCorpsTraining.setCreatedBy(user1.getId());
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

    trainingExercises.addSeries(new HashSet<>(this.seriesList));

    TrainingExercises createdTrainingExercises = this.trainingExercisesRepository.save(trainingExercises);
    this.trainingExercisesList.add(createdTrainingExercises);
  }

  private void loadSeries() {
    Series series1 = new Series();
    series1.setPositionIndex(1);
    series1.setRepsCount(8);
    series1.setRestTime("02:00");
    series1.setWeight(50);
    this.seriesList.add(series1);

    Series series2 = new Series();
    series1.setPositionIndex(2);
    series1.setRepsCount(10);
    series1.setRestTime("02:00");
    series1.setWeight(60);
    this.seriesList.add(series2);

    Series series3 = new Series();
    series1.setPositionIndex(2);
    series1.setRepsCount(10);
    series1.setRestTime("02:00");
    series1.setWeight(60);
    this.seriesList.add(series3);

    seriesRepository.saveAll(this.seriesList);
  }
}
