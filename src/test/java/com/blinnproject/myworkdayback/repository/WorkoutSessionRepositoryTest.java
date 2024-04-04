package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.entity.*;
import com.blinnproject.myworkdayback.model.enums.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkoutSessionRepositoryTest {

  @Autowired
  private WorkoutSessionRepository workoutSessionRepository;

  @Autowired
  private WorkoutModelRepository workoutModelRepository;

  @Autowired
  private WorkoutExerciseRepository workoutExerciseRepository;

  @Autowired
  private ExerciseRepository exerciseRepository;

  @Autowired
  private UserRepository userRepository;

  private User user;

  @Container
  private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11.1")
      .withDatabaseName("DB_RAISE_TEST")
      .withUsername("username")
      .withPassword("password");

  static {
    postgreSQLContainer.start();
  }

  @BeforeAll
  void beforeAllTests() {
    createMockedUser();
  }

  void createMockedUser() {
    user = new User();

    userRepository.findByUsername("mocked-user").ifPresent(data -> {
      user = data;
    });

    if (user.getId() == null) {
      user.setUsername("mocked-user");
      user.setPassword("Toto@123*");
      user.setEmail("mocked-user@email.fr");
      user.setGender(EGender.MAN);
      user = userRepository.save(user);
    }
  }

  @Test
  @Order(value = 1)
  void testConnectionToDatabase() {
    Assertions.assertNotNull(userRepository);
    Assertions.assertNotNull(workoutModelRepository);
    Assertions.assertNotNull(workoutExerciseRepository);
    Assertions.assertNotNull(exerciseRepository);
    Assertions.assertNotNull(workoutSessionRepository);
  }

   @Test
   @Order(value = 2)
   void WorkoutSessionRepository_findAllSessionByDate_ReturnWorkoutSessionList() {
    LocalDate date = LocalDate.parse("2021-09-06");
    int dayOfWeek = 2;

    // Create workout model
    WorkoutModel workoutModel = createWorkoutModel();
    List<WorkoutExercise> workoutExercises = createWorkoutExercises(workoutModel);

    // Create workout session
    WorkoutSession workoutSession = new WorkoutSession();
    workoutSession.setName("Session " + workoutModel.getName());
    workoutSession.setWorkoutModel(workoutModel);
    workoutSession.setSessionStatus(ESessionStatus.IN_PROGRESS);
    workoutSession.setStartedAt(LocalDate.parse("2021-09-06"));
    workoutSession.setCreatedBy(user.getId());
    workoutSession.setEndedAt(null);
    workoutSession.setWorkoutExerciseList(new ArrayList<>(workoutExercises));

    workoutSessionRepository.save(workoutSession);

    List<Object[]> result = workoutSessionRepository.findAllSessionByDate(dayOfWeek, LocalDate.parse("2021-09-13"), user.getId());
    assert result != null;
   }

  private WorkoutModel createWorkoutModel() {
    Schedule schedule = new Schedule();
    schedule.setCreatedBy(this.user.getId());
    schedule.setStartTime(LocalTime.parse("18:00"));
    schedule.setEndTime(LocalTime.parse("20:00"));
    schedule.setStartDate(LocalDate.parse("2021-09-06"));
    schedule.setMonday(true);
    schedule.setFriday(true);
    schedule.setFrequency(EFrequency.WEEKLY);

    WorkoutModel workoutModel = new WorkoutModel(
        "Upper Body Workout",
        "Upper body workout for beginners",
        null,
        String.valueOf(ESport.WEIGHTLIFTING),
        true,
        true,
        "icon_dumbbell",
        "#0072db"
    );
    workoutModel.setCreatedBy(this.user.getId());
    workoutModel.setSchedules(List.of(schedule));

    return workoutModelRepository.saveAndFlush(workoutModel);
  }

  private List<WorkoutExercise> createWorkoutExercises(WorkoutModel workoutModel) {
    List<WorkoutExercise> workoutExercises = new ArrayList<>();

    Exercise benchPressExercise = new Exercise();
    benchPressExercise.setName("Bench press");
    benchPressExercise.setMusclesUsed(new HashSet<>(Arrays.asList(EMuscle.PECTORALIS_MAJOR, EMuscle.TRICEPS)));
    benchPressExercise.setCreatedBy(user.getId());

    Exercise benchPressExerciseResult = exerciseRepository.saveAndFlush(benchPressExercise);

    WorkoutExercise workoutExercise1 = new WorkoutExercise();
    workoutExercise1.setExercise(benchPressExerciseResult);
    workoutExercise1.setWorkout(workoutModel);
    workoutExercise1.setPositionIndex(1);
    workoutExercise1.setNotes("Coup de pied direct");
    workoutExercise1.setNumberOfWarmUpSets(0);
    workoutExercise1.setCreatedBy(this.user.getId());

    WorkoutSet workoutSet1 = new WorkoutSet();
    workoutSet1.setRepsCount(2);
    workoutSet1.setWeight(0);
    workoutSet1.setPositionIndex(1);
    workoutSet1.setRestTime("0");
    workoutSet1.setNotes("Premier aller-retour");
    workoutSet1.setCreatedBy(this.user.getId());

    WorkoutSet workoutSet2 = new WorkoutSet();
    workoutSet2.setRepsCount(2);
    workoutSet2.setWeight(0);
    workoutSet2.setPositionIndex(2);
    workoutSet2.setRestTime("0");
    workoutSet2.setNotes("Deuxième aller-retour");
    workoutSet2.setCreatedBy(this.user.getId());

    WorkoutSet workoutSet3 = new WorkoutSet();
    workoutSet3.setRepsCount(2);
    workoutSet3.setWeight(0);
    workoutSet3.setPositionIndex(3);
    workoutSet3.setRestTime("0");
    workoutSet3.setNotes("Troisième aller-retour");
    workoutSet3.setCreatedBy(this.user.getId());

    workoutExercise1.addWorkoutSets(List.of(workoutSet1, workoutSet2, workoutSet3));

    workoutExercises.add(workoutExercise1);

    return workoutExerciseRepository.saveAll(workoutExercises);
  }
}
