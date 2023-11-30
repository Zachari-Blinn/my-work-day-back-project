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
    Training hautDuCorpsTraining = createTraining("Haut du corps", this.user.getId());
    this.trainingList.add(hautDuCorpsTraining);

    Training basDuCorpsTraining = createTraining("Bas du corps", this.user.getId());
    this.trainingList.add(basDuCorpsTraining);

    // Add more exercise here

    trainingRepository.saveAll(this.trainingList);
  }

  private Training createTraining(String name, Long userID) {
    Training training = new Training();
    training.setName(name);
    training.setCreatedBy(userID);
    return training;
  }

  public Training getTrainingByName(String name) throws ChangeSetPersister.NotFoundException {
    return this.trainingList.stream().filter(training -> training.getName().equals(name)).findFirst().orElseThrow(ChangeSetPersister.NotFoundException::new);
  }

  private void loadTrainingExercises() throws ChangeSetPersister.NotFoundException {
    this.loadSeries();

    Training hautDuCorpsTraining = this.getTrainingByName("Haut du corps");
    Exercise benchPressExercise = this.exerciseData.getExerciseByName("Bench press");

    // Training basDuCorpsTraining = this.getTrainingByName("Bas du corps");
    // todo add squat exercise

    TrainingExercises hautDuCorpsTrainingExercises = this.createTrainingExercises(hautDuCorpsTraining, benchPressExercise, "Lorem ipsum note");
    this.trainingExercisesList.add(hautDuCorpsTrainingExercises);

//    TrainingExercises basDuCorpsTrainingExercises = this.createTrainingExercises(basDuCorpsTraining, benchPressExercise, "Lorem ipsum note"); // this line crash
//    this.trainingExercisesList.add(basDuCorpsTrainingExercises);

    this.trainingExercisesRepository.saveAll(this.trainingExercisesList);
  }

  private TrainingExercises createTrainingExercises(Training training, Exercise exercise, String notes) {
    TrainingExercises trainingExercises = new TrainingExercises();

    trainingExercises.setTraining(training);
    trainingExercises.setExercise(exercise);
    trainingExercises.setNotes(notes);
    trainingExercises.setNumberOfWarmUpSeries(2);

    trainingExercises.addSeriesList(this.seriesList);
    return trainingExercises;
  }

  private void loadSeries() {
    Series series1 = createSeries(1, 8, 50, "01:00", this.user.getId());
    this.seriesList.add(series1);

    Series series2 = createSeries(2, 10, 60, "02:00", this.user.getId());
    this.seriesList.add(series2);

    Series series3 = createSeries(2, 10, 60, "02:00", this.user.getId());
    this.seriesList.add(series3);
  }

  private Series createSeries(int positionIndex, int repsCount, int weight, String restTime, Long userId) {
    Series series = new Series();
    series.setPositionIndex(positionIndex);
    series.setRepsCount(repsCount);
    series.setRestTime(restTime);
    series.setWeight(weight);
    series.setCreatedBy(userId);
    return series;
  }
}
