package com.blinnproject.myworkdayback.seed.models;

import com.blinnproject.myworkdayback.model.entity.*;
import com.blinnproject.myworkdayback.model.enums.EDayOfWeek;
import com.blinnproject.myworkdayback.repository.TrainingExercisesRepository;
import com.blinnproject.myworkdayback.repository.TrainingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class TrainingData {

  private static final Logger logger = LoggerFactory.getLogger(TrainingData.class);

  private final TrainingRepository trainingRepository;

  private final TrainingExercisesRepository trainingExercisesRepository;

  private final UserData userData;

  private final ExerciseData exerciseData;

  public TrainingData(TrainingRepository trainingRepository, TrainingExercisesRepository trainingExercisesRepository, UserData userData, ExerciseData exerciseData) {
    this.trainingRepository = trainingRepository;
    this.trainingExercisesRepository = trainingExercisesRepository;
    this.userData = userData;
    this.exerciseData = exerciseData;
  }

  List<Training> trainingList = new ArrayList<>();
  List<TrainingExercises> trainingExercisesList = new ArrayList<>();

  public User user;

  public void load() throws ChangeSetPersister.NotFoundException {
    if (trainingRepository.count() == 0) {
      logger.info("Seeding training...");

      this.user = this.userData.findUserByUsername("jean-sebastien");

      this.loadExercisesAndCreateTraining();
      this.loadTrainingExercises();

      logger.info("{} exercise successfully loaded!", trainingRepository.count());
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
    training.setIconName("icon_dumbbell");
    training.setIconHexadecimalColor("#0072db");
    training.setCreatedBy(userID);
    ArrayList<EDayOfWeek> days = new ArrayList<>();
    days.add(EDayOfWeek.TUESDAY);
    days.add(EDayOfWeek.THURSDAY);
    days.add(EDayOfWeek.WEDNESDAY);
    training.setTrainingDays(days);

    return training;
  }

  public Training getTrainingByName(String name) throws ChangeSetPersister.NotFoundException {
    return this.trainingList.stream().filter(training -> training.getName().equals(name)).findFirst().orElseThrow(ChangeSetPersister.NotFoundException::new);
  }

  private void loadTrainingExercises() throws ChangeSetPersister.NotFoundException {
    Training hautDuCorpsTraining = this.getTrainingByName("Haut du corps");

    Exercise benchPressExercise = this.exerciseData.getExerciseByName("Bench press");
    Exercise militaryPressExercise = this.exerciseData.getExerciseByName("Military Press");

    List<Series> benchPressSeriesList = new ArrayList<>();
    Series benchPressSeries1 = createSeries(8, 50, "01:00", this.user.getId());
    benchPressSeriesList.add(benchPressSeries1);
    Series benchPressSeries2 = createSeries(10, 60, "02:00", this.user.getId());
    benchPressSeriesList.add(benchPressSeries2);
    Series benchPressSeries3 = createSeries(10, 60, "02:00", this.user.getId());
    benchPressSeriesList.add(benchPressSeries3);

    List<Series> militaryPressSeriesList = new ArrayList<>();
    Series militaryPressSeries1 = createSeries(8, 50, "01:00", this.user.getId());
    militaryPressSeriesList.add(militaryPressSeries1);
    Series militaryPressSeries2 = createSeries(10, 60, "02:00", this.user.getId());
    militaryPressSeriesList.add(militaryPressSeries2);

    TrainingExercises benchPressTrainingExercises = this.createTrainingExercises(1, hautDuCorpsTraining, benchPressExercise, "Bench press note", benchPressSeriesList);
    this.trainingExercisesList.add(benchPressTrainingExercises);
    TrainingExercises militaryPressTrainingExercises = this.createTrainingExercises(2, hautDuCorpsTraining, militaryPressExercise, "Military press note", militaryPressSeriesList);
    this.trainingExercisesList.add(militaryPressTrainingExercises);

    Training basDuCorpsTraining = this.getTrainingByName("Bas du corps");

    Exercise squatExercise = this.exerciseData.getExerciseByName("Squat");

    List<Series> squatSeriesList = new ArrayList<>();
    Series squatSeries1 = createSeries(8, 50, "01:00", this.user.getId());
    squatSeriesList.add(squatSeries1);
    Series squatSeries2 = createSeries(10, 60, "02:00", this.user.getId());
    squatSeriesList.add(squatSeries2);
    Series squatSeries3 = createSeries(10, 60, "02:00", this.user.getId());
    squatSeriesList.add(squatSeries3);

    TrainingExercises squatTrainingExercises = this.createTrainingExercises(1, basDuCorpsTraining, squatExercise, "Squat note", squatSeriesList);
    this.trainingExercisesList.add(squatTrainingExercises);

    this.trainingExercisesRepository.saveAll(this.trainingExercisesList);
  }

  private TrainingExercises createTrainingExercises(int positionIndex, Training training, Exercise exercise, String notes, List<Series> seriesList) {
    TrainingExercises trainingExercises = new TrainingExercises();

    trainingExercises.setPositionIndex(positionIndex);
    trainingExercises.setTraining(training);
    trainingExercises.setExercise(exercise);
    trainingExercises.setNotes(notes);
    trainingExercises.setNumberOfWarmUpSeries(2);
    trainingExercises.addSeriesList(seriesList);

    return trainingExercises;
  }

  private Series createSeries(int repsCount, int weight, String restTime, Long userId) {
    Series series = new Series();

    series.setRepsCount(repsCount);
    series.setRestTime(restTime);
    series.setWeight(weight);
    series.setCreatedBy(userId);

    return series;
  }
}
