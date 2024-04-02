package com.blinnproject.myworkdayback.controllers;

import com.blinnproject.myworkdayback.model.entity.*;
import com.blinnproject.myworkdayback.model.enums.*;
import com.blinnproject.myworkdayback.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkoutSessionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

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

  private WorkoutModel createWorkoutModel() {
    Schedule schedule = new Schedule();
    schedule.setCreatedBy(this.user.getId());
    schedule.setStartTime(LocalTime.parse("18:00"));
    schedule.setEndTime(LocalTime.parse("20:00"));
    schedule.setStartDate(LocalDate.parse("2021-09-01"));
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

  @Test
  @WithUserDetails("mocked-user")
  void workoutSessionController_createWorkoutSessionWithoutBody_ReturnOk() throws Exception {
    // Seed data
    WorkoutModel workoutModel = createWorkoutModel();
    List<WorkoutExercise> workoutExercises = createWorkoutExercises(workoutModel);

    // Test request
    MvcResult postResult = mockMvc.perform(post("/api/workout-session/{startedAt}/workout-model/{workoutModelId}", "2021-09-03 18:15:00", workoutModel.getId())
            .contentType("application/json"))
        .andExpect(status().isCreated())
        .andReturn();

    String response = postResult.getResponse().getContentAsString();
    JsonNode jsonNode = objectMapper.readTree(response);

    // Test response
    assertEquals("IN_PROGRESS", jsonNode.at("/data/sessionStatus").asText());
    JsonNode workoutExerciseList = jsonNode.at("/data/workoutExerciseList");
    for (int i = 0; i < workoutExerciseList.size(); i++) {
      JsonNode exerciseNode = workoutExerciseList.get(i);
      assertEquals("NOT_PERFORMED", exerciseNode.at("/performStatus").asText());

      JsonNode workoutSets = exerciseNode.at("/workoutSets");
      for (int j = 0; j < workoutSets.size(); j++) {
        JsonNode setNode = workoutSets.get(j);
        assertEquals("false", setNode.at("/isPerformed").asText());
      }
    }
  }

  @Test
  void workoutSessionController_createWorkoutSessionWithoutAuth_ReturnUnauthorized() throws Exception {
    mockMvc.perform(post("/api/workout-session/{startedAt}/workout-model/{workoutModelId}", "2021-09-03 18:15:00", 1L)
            .contentType("application/json"))
        .andExpect(status().isUnauthorized());
  }
}
