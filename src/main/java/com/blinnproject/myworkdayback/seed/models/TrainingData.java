package com.blinnproject.myworkdayback.seed.models;

import com.blinnproject.myworkdayback.model.entity.*;
import com.blinnproject.myworkdayback.repository.WorkoutExerciseRepository;
import com.blinnproject.myworkdayback.repository.WorkoutSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class TrainingData {

  private static final Logger logger = LoggerFactory.getLogger(TrainingData.class);

  private final WorkoutSessionRepository trainingRepository;

  private final WorkoutExerciseRepository trainingExercisesRepository;

  private final UserData userData;

  private final ExerciseData exerciseData;

  public TrainingData(WorkoutSessionRepository trainingRepository, WorkoutExerciseRepository trainingExercisesRepository, UserData userData, ExerciseData exerciseData) {
    this.trainingRepository = trainingRepository;
    this.trainingExercisesRepository = trainingExercisesRepository;
    this.userData = userData;
    this.exerciseData = exerciseData;
  }

  List<WorkoutSession> trainingList = new ArrayList<>();
  List<WorkoutExercise> trainingExercisesList = new ArrayList<>();

  public User user;

//  public void load() throws ChangeSetPersister.NotFoundException {
//    if (trainingRepository.count() == 0) {
//      logger.info("Seeding training...");
//
//      this.user = this.userData.findUserByUsername("jean-sebastien");
//
//      this.loadExercisesAndCreateTraining();
//      this.loadTrainingExercises();
//
//      logger.info("{} exercise successfully loaded!", trainingRepository.count());
//    }
//  }
//
//  private void loadExercisesAndCreateTraining() {
//    WorkoutSession hautDuCorpsTraining = createTraining("Haut du corps", this.user.getId());
//    this.trainingList.add(hautDuCorpsTraining);
//
//    WorkoutSession basDuCorpsTraining = createTraining("Bas du corps", this.user.getId());
//    this.trainingList.add(basDuCorpsTraining);
//
//    // Add more exercise here
//
//    trainingRepository.saveAll(this.trainingList);
//  }
//
//  private WorkoutSession createTraining(String name, Long userID) {
//    WorkoutSession training = new WorkoutSession();
//    training.setName(name);
//    training.setIconName("icon_dumbbell");
//    training.setIconHexadecimalColor("#0072db");
//    training.setCreatedBy(userID);
//    ArrayList<EDayOfWeek> days = new ArrayList<>();
//    days.add(EDayOfWeek.TUESDAY);
//    days.add(EDayOfWeek.THURSDAY);
//    days.add(EDayOfWeek.WEDNESDAY);
//    training.setTrainingDays(days);
//
//    return training;
//  }
//
//  public WorkoutSession getTrainingByName(String name) throws ChangeSetPersister.NotFoundException {
//    return this.trainingList.stream().filter(training -> training.getName().equals(name)).findFirst().orElseThrow(ChangeSetPersister.NotFoundException::new);
//  }
//
//  private void loadTrainingExercises() throws ChangeSetPersister.NotFoundException {
//    WorkoutSession hautDuCorpsTraining = this.getTrainingByName("Haut du corps");
//
//    Exercise benchPressExercise = this.exerciseData.getExerciseByName("Bench press");
//    Exercise militaryPressExercise = this.exerciseData.getExerciseByName("Military Press");
//
//    List<WorkoutSet> benchPressSeriesList = new ArrayList<>();
//    WorkoutSet benchPressSeries1 = createSeries(8, 50, "01:00", this.user.getId());
//    benchPressSeriesList.add(benchPressSeries1);
//    WorkoutSet benchPressSeries2 = createSeries(10, 60, "02:00", this.user.getId());
//    benchPressSeriesList.add(benchPressSeries2);
//    WorkoutSet benchPressSeries3 = createSeries(10, 60, "02:00", this.user.getId());
//    benchPressSeriesList.add(benchPressSeries3);
//
//    List<WorkoutSet> militaryPressSeriesList = new ArrayList<>();
//    WorkoutSet militaryPressSeries1 = createSeries(8, 50, "01:00", this.user.getId());
//    militaryPressSeriesList.add(militaryPressSeries1);
//    WorkoutSet militaryPressSeries2 = createSeries(10, 60, "02:00", this.user.getId());
//    militaryPressSeriesList.add(militaryPressSeries2);
//
//    WorkoutExercise benchPressTrainingExercises = this.createTrainingExercises(1, hautDuCorpsTraining, benchPressExercise, "Bench press note", benchPressSeriesList);
//    this.trainingExercisesList.add(benchPressTrainingExercises);
//    WorkoutExercise militaryPressTrainingExercises = this.createTrainingExercises(2, hautDuCorpsTraining, militaryPressExercise, "Military press note", militaryPressSeriesList);
//    this.trainingExercisesList.add(militaryPressTrainingExercises);
//
//    WorkoutSession basDuCorpsTraining = this.getTrainingByName("Bas du corps");
//
//    Exercise squatExercise = this.exerciseData.getExerciseByName("Squat");
//
//    List<WorkoutSet> squatSeriesList = new ArrayList<>();
//    WorkoutSet squatSeries1 = createSeries(8, 50, "01:00", this.user.getId());
//    squatSeriesList.add(squatSeries1);
//    WorkoutSet squatSeries2 = createSeries(10, 60, "02:00", this.user.getId());
//    squatSeriesList.add(squatSeries2);
//    WorkoutSet squatSeries3 = createSeries(10, 60, "02:00", this.user.getId());
//    squatSeriesList.add(squatSeries3);
//
//    WorkoutExercise squatTrainingExercises = this.createTrainingExercises(1, basDuCorpsTraining, squatExercise, "Squat note", squatSeriesList);
//    this.trainingExercisesList.add(squatTrainingExercises);
//
//    this.trainingExercisesRepository.saveAll(this.trainingExercisesList);
//  }
//
//  private WorkoutExercise createTrainingExercises(int positionIndex, WorkoutSession training, Exercise exercise, String notes, List<WorkoutSet> seriesList) {
//    WorkoutExercise trainingExercises = new WorkoutExercise();
//
//    trainingExercises.setPositionIndex(positionIndex);
//    trainingExercises.setTraining(training);
//    trainingExercises.setExercise(exercise);
//    trainingExercises.setNotes(notes);
//    trainingExercises.setNumberOfWarmUpSeries(2);
//    trainingExercises.addSeriesList(seriesList);
//
//    return trainingExercises;
//  }
//
//  private WorkoutSet createSeries(int repsCount, int weight, String restTime, Long userId) {
//    WorkoutSet series = new WorkoutSet();
//
//    series.setRepsCount(repsCount);
//    series.setRestTime(restTime);
//    series.setWeight(weight);
//    series.setCreatedBy(userId);
//
//    return series;
//  }
}
