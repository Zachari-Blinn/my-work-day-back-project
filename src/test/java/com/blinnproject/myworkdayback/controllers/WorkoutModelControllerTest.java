package com.blinnproject.myworkdayback.controllers;

import com.blinnproject.myworkdayback.model.dto.ScheduleCreateDTO;
import com.blinnproject.myworkdayback.model.dto.WorkoutExerciseCreateDTO;
import com.blinnproject.myworkdayback.model.dto.WorkoutModelCreateDTO;
import com.blinnproject.myworkdayback.model.entity.Exercise;
import com.blinnproject.myworkdayback.model.entity.User;
import com.blinnproject.myworkdayback.model.entity.WorkoutModel;
import com.blinnproject.myworkdayback.model.entity.WorkoutSet;
import com.blinnproject.myworkdayback.model.enums.EFrequency;
import com.blinnproject.myworkdayback.model.enums.ESport;
import com.blinnproject.myworkdayback.repository.ExerciseRepository;
import com.blinnproject.myworkdayback.repository.WorkoutModelRepository;
import com.blinnproject.myworkdayback.model.enums.EGender;
import com.blinnproject.myworkdayback.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkoutModelControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private WorkoutModelRepository workoutModelRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ExerciseRepository exerciseRepository;

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

  @Test
  @WithUserDetails("mocked-user")
  void WorkoutModelController_Create_ReturnSavedWorkoutModel() throws Exception {
     WorkoutModelCreateDTO workoutModel = new WorkoutModelCreateDTO(
        "Quantum Slam",
        "Quantum Slam",
         String.valueOf(ESport.MMA),
        true,
        true,
        "icon_dumbbell",
        "#0072db"
     );

    mockMvc.perform(post("/api/workout-model")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(workoutModel)))
        .andExpect(status().isCreated());

    WorkoutModel workoutModelCreated = workoutModelRepository.findByName("Quantum Slam").orElse(null);
    assert(Objects.requireNonNull(workoutModelCreated).getName()).equals("Quantum Slam");
    assert(Objects.requireNonNull(workoutModelCreated).getDescription()).equals("Quantum Slam");
    assert(Objects.requireNonNull(workoutModelCreated).getSportPreset()).equals(ESport.MMA);
    assert(Objects.requireNonNull(workoutModelCreated).getHasWarmUp()).equals(true);
    assert(Objects.requireNonNull(workoutModelCreated).getHasStretching()).equals(true);
    assert(Objects.requireNonNull(workoutModelCreated).getIconName()).equals("icon_dumbbell");
    assert(Objects.requireNonNull(workoutModelCreated).getIconHexadecimalColor()).equals("#0072db");
  }

  @Test
  @WithUserDetails("mocked-user")
  void WorkoutModelController_GetWorkoutModelById_ReturnWorkoutModel() throws Exception {
    WorkoutModelCreateDTO workoutModel = new WorkoutModelCreateDTO(
        "Infinicourse",
        "Infinicourse",
        String.valueOf(ESport.MMA),
        true,
        true,
        "icon_dumbbell",
        "#0072db"
    );

    mockMvc.perform(post("/api/workout-model")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(workoutModel)))
        .andExpect(status().isCreated());

    WorkoutModel workoutModelCreated = workoutModelRepository.findByName("Infinicourse").orElse(null);
    assert(Objects.requireNonNull(workoutModelCreated).getName()).equals("Infinicourse");
    assert(Objects.requireNonNull(workoutModelCreated).getDescription()).equals("Infinicourse");
    assert(Objects.requireNonNull(workoutModelCreated).getSportPreset()).equals(ESport.MMA);
    assert(Objects.requireNonNull(workoutModelCreated).getHasWarmUp()).equals(true);
    assert(Objects.requireNonNull(workoutModelCreated).getHasStretching()).equals(true);
    assert(Objects.requireNonNull(workoutModelCreated).getIconName()).equals("icon_dumbbell");
    assert(Objects.requireNonNull(workoutModelCreated).getIconHexadecimalColor()).equals("#0072db");

    mockMvc.perform(get("/api/workout-model/" + workoutModelCreated.getId()))
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails("mocked-user")
  void WorkoutModelController_GetWorkoutModelById_WhenNotExists_ReturnsNotFound() throws Exception {
    mockMvc.perform(get("/api/workout-model/{id}", "42"))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithUserDetails("mocked-user")
  void WorkoutModelController_addExerciseToWorkoutModelWithoutSets_ReturnsWorkoutModelWithExercise() throws Exception {
    WorkoutModelCreateDTO workoutModel = new WorkoutModelCreateDTO(
        "Trick War",
        "Trick War",
        String.valueOf(ESport.MMA),
        true,
        true,
        "icon_dumbbell",
        "#0072db"
    );

    MvcResult workoutModelResult = mockMvc.perform(post("/api/workout-model")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(workoutModel)))
        .andExpect(status().isCreated())
      .andReturn();
    long workoutModelId = objectMapper.readTree(workoutModelResult.getResponse().getContentAsString()).at("/data/id").asLong();

    WorkoutExerciseCreateDTO workoutExercise = new WorkoutExerciseCreateDTO(
        1,
        "Punching",
        2,
        null
    );

    mockMvc.perform(post("/api/workout-model/" + workoutModelId + "/exercise/1")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(workoutExercise)))
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails("mocked-user")
  void WorkoutModelController_addExerciseToWorkoutModelWithSets_ReturnsWorkoutModelWithExercise() throws Exception {
    WorkoutModelCreateDTO workoutModel = new WorkoutModelCreateDTO(
        "Rosurf",
        "Rosurf",
        String.valueOf(ESport.MMA),
        true,
        true,
        "icon_dumbbell",
        "#0072db"
    );

    MvcResult workoutModelResult = mockMvc.perform(post("/api/workout-model")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(workoutModel)))
      .andExpect(status().isCreated())
      .andReturn();
    long workoutModelId = objectMapper.readTree(workoutModelResult.getResponse().getContentAsString()).at("/data/id").asLong();

    WorkoutSet workoutSet1 = new WorkoutSet(
        1,
        null,
        10,
        "10",
        "10",
        10
    );

    WorkoutSet workoutSet2 = new WorkoutSet(
        2,
        null,
        10,
        "10",
        "10",
        10
    );

    WorkoutExerciseCreateDTO workoutExercise = new WorkoutExerciseCreateDTO(
        1,
        "Punching",
        2,
        new ArrayList<>(Arrays.asList(workoutSet1, workoutSet2))
    );

    mockMvc.perform(post("/api/workout-model/" + workoutModelId + "/exercise/1")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(workoutExercise)))
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails("mocked-user")
  void WorkoutModelController_removeExerciseFromWorkoutModel_ReturnsWorkoutModelWithoutExercise() throws Exception {
    WorkoutModelCreateDTO workoutModel = new WorkoutModelCreateDTO(
        "Demolition Draft",
        "Demolition Draft",
        String.valueOf(ESport.MMA),
        true,
        true,
        "icon_dumbbell",
        "#0072db"
    );

    MvcResult workoutModelResult = mockMvc.perform(post("/api/workout-model")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(workoutModel)))
      .andExpect(status().isCreated())
      .andReturn();
    long workoutModelId = objectMapper.readTree(workoutModelResult.getResponse().getContentAsString()).at("/data/id").asLong();

    WorkoutSet workoutSet1 = new WorkoutSet(
        1,
        null,
        10,
        "10",
        "10",
        10
    );

    WorkoutSet workoutSet2 = new WorkoutSet(
        2,
        null,
        10,
        "10",
        "10",
        10
    );

    WorkoutExerciseCreateDTO workoutExercise = new WorkoutExerciseCreateDTO(
        1,
        "Punching",
        2,
        new ArrayList<>(Arrays.asList(workoutSet1, workoutSet2))
    );

    Exercise exercise = new Exercise(
      "Demolition Draft exercise",
      null
    );
    Exercise savedExercise = exerciseRepository.saveAndFlush(exercise);

    MvcResult workoutExerciseResult = mockMvc.perform(post("/api/workout-model/" + workoutModelId + "/exercise/" + savedExercise.getId())
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(workoutExercise)))
        .andExpect(status().isOk())
      .andReturn();

    mockMvc.perform(get("/api/workout-model/" + workoutModelId))
        .andExpect(status().isOk())
      ;

    String response = workoutExerciseResult.getResponse().getContentAsString();
    JsonNode jsonNode = objectMapper.readTree(response);
    long workoutExerciseId = jsonNode.at("/data/id").asLong();

    mockMvc.perform(delete("/api/workout-model/exercise/" + workoutExerciseId))
        .andExpect(status().isOk());

    mockMvc.perform(get("/api/workout-model/" + workoutModelId))
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails("mocked-user")
  void WorkoutModelController_deleteWorkoutModel_ReturnsWorkoutModelDeleted() throws Exception {
    WorkoutModelCreateDTO workoutModel = new WorkoutModelCreateDTO(
        "Grimglide",
        "Grimglide",
        String.valueOf(ESport.MMA),
        true,
        true,
        "icon_dumbbell",
        "#0072db"
    );

    MvcResult workoutModelResult = mockMvc.perform(post("/api/workout-model")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(workoutModel)))
        .andExpect(status().isCreated())
      .andReturn();
    long workoutModelId = objectMapper.readTree(workoutModelResult.getResponse().getContentAsString()).at("/data/id").asLong();

    mockMvc.perform(delete("/api/workout-model/{id}", workoutModelId))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithUserDetails("mocked-user")
  void WorkoutModelController_deleteWorkoutModel_WhenNotExists_ReturnsNotFound() throws Exception {
    mockMvc.perform(delete("/api/workout-model/{id}", "42"))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithUserDetails("mocked-user")
  void WorkoutModelController_findAllWorkoutModel_ReturnsAllWorkoutModels() throws Exception {
    WorkoutModelCreateDTO workoutModel1 = new WorkoutModelCreateDTO(
        "MMA",
        "Mixed Martial Arts",
        String.valueOf(ESport.MMA),
        true,
        true,
        "icon_dumbbell",
        "#0072db"
    );

    WorkoutModelCreateDTO workoutModel2 = new WorkoutModelCreateDTO(
        "Boxing",
        "Boxing",
        String.valueOf(ESport.BOXING),
        true,
        true,
        "icon_dumbbell",
        "#0072db"
    );

    mockMvc.perform(post("/api/workout-model")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(workoutModel1)))
        .andExpect(status().isCreated());

    mockMvc.perform(post("/api/workout-model")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(workoutModel2)))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/api/workout-model"))
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails("mocked-user")
  void WorkoutModelController_updateWorkoutModel_ReturnsUpdatedWorkoutModel() throws Exception {
    WorkoutModelCreateDTO workoutModel = new WorkoutModelCreateDTO(
        "Amadraft",
        "Amadraft",
        String.valueOf(ESport.WEIGHTLIFTING),
        true,
        true,
        "icon_dumbbell",
        "#0072db"
    );

    MvcResult workoutModelResult = mockMvc.perform(post("/api/workout-model")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(workoutModel)))
        .andExpect(status().isCreated()).andReturn();
    long workoutModelId = objectMapper.readTree(workoutModelResult.getResponse().getContentAsString()).at("/data/id").asLong();

    WorkoutModelCreateDTO updatedWorkoutModel = new WorkoutModelCreateDTO(
        "New Amadraft",
        "New amadraft",
        String.valueOf(ESport.MMA),
        false,
        false,
        "icon_dumbbell",
        "#0072db"
    );

    mockMvc.perform(patch("/api/workout-model/" + workoutModelId)
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(updatedWorkoutModel)))
        .andExpect(status().isOk());

    WorkoutModel updatedWorkoutModelCreated = workoutModelRepository.findByName("New Amadraft").orElse(null);
    assert updatedWorkoutModelCreated != null;
    assert updatedWorkoutModelCreated.getHasWarmUp().equals(false);
    assert updatedWorkoutModelCreated.getHasStretching().equals(false);
    assert updatedWorkoutModelCreated.getLastUpdatedOn() != null;
    assert updatedWorkoutModelCreated.getLastUpdatedBy() != null;
  }

  @Test
  @WithUserDetails("mocked-user")
  void WorkoutModelController_updateWorkoutModel_WhenNotExists_ReturnsNotFound() throws Exception {
    WorkoutModelCreateDTO updatedWorkoutModel = new WorkoutModelCreateDTO(
        "MMA",
        "Mixed Martial Arts",
        String.valueOf(ESport.MMA),
        false,
        false,
        "icon_dumbbell",
        "#0072db"
    );

    mockMvc.perform(patch("/api/workout-model/{id}", "42")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(updatedWorkoutModel)))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithUserDetails("mocked-user")
  void WorkoutModelController_findAllExercises_ReturnsAllExercises() throws Exception {
    WorkoutModelCreateDTO workoutModel = new WorkoutModelCreateDTO(
        "Vitaliglide",
        "Vitaliglide",
        String.valueOf(ESport.MMA),
        true,
        true,
        "icon_dumbbell",
        "#0072db"
    );

    MvcResult workoutModelResult = mockMvc.perform(post("/api/workout-model")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(workoutModel)))
        .andExpect(status().isCreated())
      .andReturn();
    long workoutModelId = objectMapper.readTree(workoutModelResult.getResponse().getContentAsString()).at("/data/id").asLong();

    mockMvc.perform(get("/api/workout-model/" + workoutModelId + "/exercises"))
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails("mocked-user")
  void WorkoutModelController_findAllExercises_WhenNotExists_ReturnsNotNoneExercise() throws Exception {
    mockMvc.perform(get("/api/workout-model/{id}/exercises", "42"))
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails("mocked-user")
  void WorkoutModelController_addScheduleToWorkoutModel_ReturnsWorkoutModelWithSchedule() throws Exception {
    WorkoutModelCreateDTO workoutModel = new WorkoutModelCreateDTO(
        "KUNG FU",
        "Martial Arts",
        String.valueOf(ESport.MMA),
        true,
        true,
        "icon_dumbbell",
        "#0072db"
    );

    MvcResult workoutModelResult = mockMvc.perform(post("/api/workout-model")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(workoutModel)))
      .andExpect(status().isCreated())
      .andReturn();
    long workoutModelId = objectMapper.readTree(workoutModelResult.getResponse().getContentAsString()).at("/data/id").asLong();

    ScheduleCreateDTO schedule = new ScheduleCreateDTO(
        LocalDate.parse("2023-12-01"),
        LocalDate.parse("2023-12-01"),
        LocalTime.parse("06:00:00"),
        LocalTime.parse("07:00:00"),
        EFrequency.WEEKLY,
        true,
        false,
        false,
        false,
        true,
        true,
        false
    );

    mockMvc.perform(post("/api/workout-model/" + workoutModelId + "/schedule")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(schedule)))
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails("mocked-user")
  void WorkoutModelController_removeScheduleFromWorkoutModel_ReturnsWorkoutModelWithoutSchedule() throws Exception {
    WorkoutModelCreateDTO workoutModel = new WorkoutModelCreateDTO(
        "Icemix",
        "Icemix",
        String.valueOf(ESport.MMA),
        true,
        true,
        "icon_dumbbell",
        "#0072db"
    );

    MvcResult workoutModelResult = mockMvc.perform(post("/api/workout-model")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(workoutModel)))
        .andExpect(status().isCreated())
      .andReturn();
    long workoutModelId = objectMapper.readTree(workoutModelResult.getResponse().getContentAsString()).at("/data/id").asLong();

    ScheduleCreateDTO schedule = new ScheduleCreateDTO(
        LocalDate.parse("2023-12-01"),
        LocalDate.parse("2023-12-01"),
        LocalTime.parse("06:00:00"),
        LocalTime.parse("07:00:00"),
        EFrequency.WEEKLY,
        true,
        false,
        false,
        false,
        true,
        true,
        false
    );

    mockMvc.perform(post("/api/workout-model/" + workoutModelId + "/schedule")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(schedule)))
        .andExpect(status().isOk());

    mockMvc.perform(get("/api/workout-model/" + workoutModelId))
        .andExpect(status().isOk());

    mockMvc.perform(delete("/api/workout-model/schedule/1"))
        .andExpect(status().isOk());

    mockMvc.perform(get("/api/workout-model/" + workoutModelId))
        .andExpect(status().isOk());
  }
}
