package com.blinnproject.myworkdayback.seed.models;

import com.blinnproject.myworkdayback.model.entity.*;
import com.blinnproject.myworkdayback.model.enums.EFrequency;
import com.blinnproject.myworkdayback.model.enums.ESport;
import com.blinnproject.myworkdayback.repository.WorkoutExerciseRepository;
import com.blinnproject.myworkdayback.repository.WorkoutModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class WorkoutModelData {
  private static final Logger logger = LoggerFactory.getLogger(WorkoutModelData.class);
  private final WorkoutModelRepository workoutModelRepository;

  private final ExerciseData exerciseData;

  private final UserData userData;

  private User user;
  private final List<WorkoutModel> workoutModels = new ArrayList<>();
  private final WorkoutExerciseRepository workoutExerciseRepository;

  public WorkoutModelData(WorkoutModelRepository workoutModelRepository, UserData userData, ExerciseData exerciseData,
                          WorkoutExerciseRepository workoutExerciseRepository) {
    this.workoutModelRepository = workoutModelRepository;
    this.userData = userData;
    this.exerciseData = exerciseData;
    this.workoutExerciseRepository = workoutExerciseRepository;
  }

  public void load() throws ChangeSetPersister.NotFoundException {
    if (workoutModelRepository.count() == 0) {
      logger.info("Seeding workout model...");

      this.user = this.userData.findUserByUsername("jean-sebastien");

      this.loadWorkoutModel();
      this.loadWorkoutExercise();

      logger.info("{} workout model successfully loaded!", workoutModelRepository.count());
    }
  }

  private List<Schedule> loadKungFuSchedule() {
    List<Schedule> schedules = new ArrayList<>();

    Schedule kungFuScheduleMondayAndFriday = new Schedule();
    kungFuScheduleMondayAndFriday.setCreatedBy(this.user.getId());
    kungFuScheduleMondayAndFriday.setStartTime(LocalTime.parse("18:00"));
    kungFuScheduleMondayAndFriday.setEndTime(LocalTime.parse("20:00"));
    kungFuScheduleMondayAndFriday.setStartDate(LocalDate.parse("2021-09-01"));
    kungFuScheduleMondayAndFriday.setMonday(true);
    kungFuScheduleMondayAndFriday.setTuesday(false);
    kungFuScheduleMondayAndFriday.setWednesday(false);
    kungFuScheduleMondayAndFriday.setThursday(false);
    kungFuScheduleMondayAndFriday.setFriday(true);
    kungFuScheduleMondayAndFriday.setSaturday(false);
    kungFuScheduleMondayAndFriday.setSunday(false);
    kungFuScheduleMondayAndFriday.setFrequency(EFrequency.WEEKLY);

    Schedule kungFuScheduleSaturday = new Schedule();
    kungFuScheduleSaturday.setCreatedBy(this.user.getId());
    kungFuScheduleSaturday.setStartTime(LocalTime.parse("09:30"));
    kungFuScheduleSaturday.setEndTime(LocalTime.parse("12:30"));
    kungFuScheduleSaturday.setStartDate(LocalDate.parse("2021-09-01"));
    kungFuScheduleSaturday.setMonday(false);
    kungFuScheduleSaturday.setTuesday(false);
    kungFuScheduleSaturday.setWednesday(false);
    kungFuScheduleSaturday.setThursday(false);
    kungFuScheduleSaturday.setFriday(false);
    kungFuScheduleSaturday.setSaturday(true);
    kungFuScheduleSaturday.setSunday(false);
    kungFuScheduleSaturday.setFrequency(EFrequency.WEEKLY);

    schedules.add(kungFuScheduleMondayAndFriday);
    schedules.add(kungFuScheduleSaturday);

    return schedules;
  }

  public void loadWorkoutModel() {
    WorkoutModel kungFuWorkoutModel = new WorkoutModel(
        "Kung Fu",
        "Martial Arts",
        null,
        String.valueOf(ESport.KUNG_FU),
        true,
        true,
        "icon_sports_martial_arts",
        "#0072db"
    );
    kungFuWorkoutModel.setCreatedBy(this.user.getId());
    kungFuWorkoutModel.setSchedules(loadKungFuSchedule());

    WorkoutModel weightliftingWorkoutModel = new WorkoutModel(
        "weightlifting",
        "weightlifting",
        null,
        String.valueOf(ESport.WEIGHTLIFTING),
        true,
        true,
        "icon_dumbbell",
        "#0072db"
    );
    weightliftingWorkoutModel.setCreatedBy(this.user.getId());

    WorkoutModel runningWorkoutModel = new WorkoutModel(
        "running",
        "running",
        null,
        String.valueOf(ESport.RUNNING),
        true,
        true,
        "icon_dumbbell",
        "#0072db"
    );
    runningWorkoutModel.setCreatedBy(this.user.getId());

    workoutModels.add(kungFuWorkoutModel);
    workoutModels.add(weightliftingWorkoutModel);
    workoutModels.add(runningWorkoutModel);

    this.workoutModelRepository.saveAll(workoutModels);
  }

  public void loadWorkoutExercise() throws ChangeSetPersister.NotFoundException {
    List<WorkoutExercise> kungFuworkoutExercises = new ArrayList<>();

    Exercise tanTuiCrossing = this.exerciseData.getExerciseByName("Tan Tui Crossing");

    WorkoutExercise tanTuiCrossingWorkoutExercise = new WorkoutExercise();
    tanTuiCrossingWorkoutExercise.setExercise(tanTuiCrossing);
    tanTuiCrossingWorkoutExercise.setWorkout(this.getWorkoutModelByName("Kung Fu"));
    tanTuiCrossingWorkoutExercise.setPositionIndex(1);
    tanTuiCrossingWorkoutExercise.setNotes("Coup de pied direct");
    tanTuiCrossingWorkoutExercise.setNumberOfWarmUpSets(0);
    tanTuiCrossingWorkoutExercise.setCreatedBy(this.user.getId());

    WorkoutSet tanTuiCrossingWorkoutSet1 = new WorkoutSet();
    tanTuiCrossingWorkoutSet1.setRepsCount(2);
    tanTuiCrossingWorkoutSet1.setWeight(0);
    tanTuiCrossingWorkoutSet1.setPositionIndex(1);
    tanTuiCrossingWorkoutSet1.setRestTime("0");
    tanTuiCrossingWorkoutSet1.setNotes("Premier aller-retour");
    tanTuiCrossingWorkoutSet1.setCreatedBy(this.user.getId());

    WorkoutSet tanTuiCrossingWorkoutSet2 = new WorkoutSet();
    tanTuiCrossingWorkoutSet2.setRepsCount(2);
    tanTuiCrossingWorkoutSet2.setWeight(0);
    tanTuiCrossingWorkoutSet2.setPositionIndex(2);
    tanTuiCrossingWorkoutSet2.setRestTime("0");
    tanTuiCrossingWorkoutSet2.setNotes("Deuxième aller-retour");
    tanTuiCrossingWorkoutSet2.setCreatedBy(this.user.getId());

    WorkoutSet tanTuiCrossingWorkoutSet3 = new WorkoutSet();
    tanTuiCrossingWorkoutSet3.setRepsCount(2);
    tanTuiCrossingWorkoutSet3.setWeight(0);
    tanTuiCrossingWorkoutSet3.setPositionIndex(3);
    tanTuiCrossingWorkoutSet3.setRestTime("0");
    tanTuiCrossingWorkoutSet3.setNotes("Troisième aller-retour");
    tanTuiCrossingWorkoutSet3.setCreatedBy(this.user.getId());

    tanTuiCrossingWorkoutExercise.addWorkoutSets(List.of(tanTuiCrossingWorkoutSet1, tanTuiCrossingWorkoutSet2, tanTuiCrossingWorkoutSet3));

    kungFuworkoutExercises.add(tanTuiCrossingWorkoutExercise);

    // second workout exercise
    Exercise wayBaiTuiCrossing = this.exerciseData.getExerciseByName("Wai Bai Tui Crossing");

    WorkoutExercise wayBaiTuiCrossingWorkoutExercise = new WorkoutExercise();
    wayBaiTuiCrossingWorkoutExercise.setExercise(wayBaiTuiCrossing);
    wayBaiTuiCrossingWorkoutExercise.setWorkout(this.getWorkoutModelByName("Kung Fu"));
    wayBaiTuiCrossingWorkoutExercise.setPositionIndex(1);
    wayBaiTuiCrossingWorkoutExercise.setNotes("Pied en dégagement extérieur");
    wayBaiTuiCrossingWorkoutExercise.setNumberOfWarmUpSets(0);
    wayBaiTuiCrossingWorkoutExercise.setCreatedBy(this.user.getId());

    WorkoutSet wayBaiTuiCrossingWorkoutSet1 = new WorkoutSet();
    wayBaiTuiCrossingWorkoutSet1.setRepsCount(2);
    wayBaiTuiCrossingWorkoutSet1.setWeight(0);
    wayBaiTuiCrossingWorkoutSet1.setPositionIndex(1);
    wayBaiTuiCrossingWorkoutSet1.setRestTime("0");
    wayBaiTuiCrossingWorkoutSet1.setNotes("Premier aller-retour");
    wayBaiTuiCrossingWorkoutSet1.setCreatedBy(this.user.getId());

    WorkoutSet wayBaiTuiCrossingWorkoutSet2 = new WorkoutSet();
    wayBaiTuiCrossingWorkoutSet2.setRepsCount(2);
    wayBaiTuiCrossingWorkoutSet2.setWeight(0);
    wayBaiTuiCrossingWorkoutSet2.setPositionIndex(2);
    wayBaiTuiCrossingWorkoutSet2.setRestTime("0");
    wayBaiTuiCrossingWorkoutSet2.setNotes("Deuxième aller-retour");
    wayBaiTuiCrossingWorkoutSet2.setCreatedBy(this.user.getId());

    WorkoutSet wayBaiTuiCrossingWorkoutSet3 = new WorkoutSet();
    wayBaiTuiCrossingWorkoutSet3.setRepsCount(2);
    wayBaiTuiCrossingWorkoutSet3.setWeight(0);
    wayBaiTuiCrossingWorkoutSet3.setPositionIndex(3);
    wayBaiTuiCrossingWorkoutSet3.setRestTime("0");
    wayBaiTuiCrossingWorkoutSet3.setNotes("Troisième aller-retour");
    wayBaiTuiCrossingWorkoutSet3.setCreatedBy(this.user.getId());

    wayBaiTuiCrossingWorkoutExercise.addWorkoutSets(List.of(wayBaiTuiCrossingWorkoutSet1, wayBaiTuiCrossingWorkoutSet2, wayBaiTuiCrossingWorkoutSet3));

    kungFuworkoutExercises.add(wayBaiTuiCrossingWorkoutExercise);

    workoutExerciseRepository.saveAll(kungFuworkoutExercises);
  }

  public WorkoutModel getWorkoutModelByName(String name) throws ChangeSetPersister.NotFoundException {
    return this.workoutModels.stream().filter(workoutModel -> workoutModel.getName().equals(name)).findFirst().orElseThrow(ChangeSetPersister.NotFoundException::new);
  }
}
