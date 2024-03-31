package com.blinnproject.myworkdayback.controllers;

import com.blinnproject.myworkdayback.model.dto.ScheduleCreateDTO;
import com.blinnproject.myworkdayback.model.dto.WorkoutExerciseCreateDTO;
import com.blinnproject.myworkdayback.model.dto.WorkoutModelCreateDTO;
import com.blinnproject.myworkdayback.model.entity.User;
import com.blinnproject.myworkdayback.model.entity.WorkoutModel;
import com.blinnproject.myworkdayback.model.entity.WorkoutSet;
import com.blinnproject.myworkdayback.model.enums.EFrequency;
import com.blinnproject.myworkdayback.model.enums.ESport;
import com.blinnproject.myworkdayback.repository.WorkoutModelRepository;
import com.blinnproject.myworkdayback.model.enums.EGender;
import com.blinnproject.myworkdayback.repository.UserRepository;
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

  @BeforeAll
  void beforeAllTests() {
    createMockedUser();
  }

  void createMockedUser() {
    User user = new User();
    user.setUsername("mocked-user");
    user.setPassword("Toto@123*");
    user.setEmail("mocked-user@email.fr");
    user.setGender(EGender.MAN);
    userRepository.save(user);
  }

  @Test
  @WithUserDetails("mocked-user")
  void WorkoutModelController_Create_ReturnSavedWorkoutModel() throws Exception {
     WorkoutModelCreateDTO workoutModel = new WorkoutModelCreateDTO(
        "MMA",
        "Mixed Martial Arts",
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

    WorkoutModel workoutModelCreated = workoutModelRepository.findByName("MMA").orElse(null);
    assert(Objects.requireNonNull(workoutModelCreated).getName()).equals("MMA");
    assert(Objects.requireNonNull(workoutModelCreated).getDescription()).equals("Mixed Martial Arts");
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
        "MMA",
        "Mixed Martial Arts",
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

    WorkoutModel workoutModelCreated = workoutModelRepository.findByName("MMA").orElse(null);
    assert(Objects.requireNonNull(workoutModelCreated).getName()).equals("MMA");
    assert(Objects.requireNonNull(workoutModelCreated).getDescription()).equals("Mixed Martial Arts");
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
        "MMA",
        "Mixed Martial Arts",
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

    WorkoutExerciseCreateDTO workoutExercise = new WorkoutExerciseCreateDTO(
        1,
        "Punching",
        2,
        null
    );

    WorkoutModel workoutModelCreated = workoutModelRepository.findByName("MMA").orElse(null);
    assert workoutModelCreated != null;

    mockMvc.perform(post("/api/workout-model/" + workoutModelCreated.getId() + "/exercise/1")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(workoutExercise)))
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails("mocked-user")
  void WorkoutModelController_addExerciseToWorkoutModelWithSets_ReturnsWorkoutModelWithExercise() throws Exception {
    WorkoutModelCreateDTO workoutModel = new WorkoutModelCreateDTO(
        "MMA",
        "Mixed Martial Arts",
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

    WorkoutSet workoutSet1 = new WorkoutSet(
        1,
        10,
        "10",
        "10",
        10
    );

    WorkoutSet workoutSet2 = new WorkoutSet(
        2,
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

    WorkoutModel workoutModelCreated = workoutModelRepository.findByName("MMA").orElse(null);
    assert workoutModelCreated != null;

    mockMvc.perform(post("/api/workout-model/" + workoutModelCreated.getId() + "/exercise/1")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(workoutExercise)))
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails("mocked-user")
  void WorkoutModelController_removeExerciseFromWorkoutModel_ReturnsWorkoutModelWithoutExercise() throws Exception {
    WorkoutModelCreateDTO workoutModel = new WorkoutModelCreateDTO(
        "MMA",
        "Mixed Martial Arts",
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

    WorkoutSet workoutSet1 = new WorkoutSet(
        1,
        10,
        "10",
        "10",
        10
    );

    WorkoutSet workoutSet2 = new WorkoutSet(
        2,
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

    WorkoutModel workoutModelCreated = workoutModelRepository.findByName("MMA").orElse(null);
    assert workoutModelCreated != null;

    mockMvc.perform(post("/api/workout-model/" + workoutModelCreated.getId() + "/exercise/1")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(workoutExercise)))
        .andExpect(status().isOk());

    mockMvc.perform(get("/api/workout-model/" + workoutModelCreated.getId()))
        .andExpect(status().isOk());

    mockMvc.perform(delete("/api/workout-model/exercise/1"))
        .andExpect(status().isOk());

    mockMvc.perform(get("/api/workout-model/" + workoutModelCreated.getId()))
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails("mocked-user")
  void WorkoutModelController_deleteWorkoutModel_ReturnsWorkoutModelDeleted() throws Exception {
    WorkoutModelCreateDTO workoutModel = new WorkoutModelCreateDTO(
        "MMA",
        "Mixed Martial Arts",
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

    WorkoutModel workoutModelCreated = workoutModelRepository.findByName("MMA").orElse(null);
    assert workoutModelCreated != null;

    mockMvc.perform(delete("/api/workout-model/" + workoutModelCreated.getId()))
        .andExpect(status().isOk());
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
        "MMA",
        "Mixed Martial Arts",
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

    WorkoutModel workoutModelCreated = workoutModelRepository.findByName("MMA").orElse(null);
    assert workoutModelCreated != null;

    WorkoutModelCreateDTO updatedWorkoutModel = new WorkoutModelCreateDTO(
        "MMA",
        "Mixed Martial Arts",
        String.valueOf(ESport.MMA),
        false,
        false,
        "icon_dumbbell",
        "#0072db"
    );

    mockMvc.perform(patch("/api/workout-model/" + workoutModelCreated.getId())
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(updatedWorkoutModel)))
        .andExpect(status().isOk());

    WorkoutModel updatedWorkoutModelCreated = workoutModelRepository.findByName("MMA").orElse(null);
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
        "MMA",
        "Mixed Martial Arts",
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

    WorkoutModel workoutModelCreated = workoutModelRepository.findByName("MMA").orElse(null);
    assert workoutModelCreated != null;

    mockMvc.perform(get("/api/workout-model/" + workoutModelCreated.getId() + "/exercises"))
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

    mockMvc.perform(post("/api/workout-model")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(workoutModel)))
        .andExpect(status().isCreated());

    WorkoutModel workoutModelCreated = workoutModelRepository.findByName("KUNG FU").orElse(null);
    assert workoutModelCreated != null;

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

    mockMvc.perform(post("/api/workout-model/" + workoutModelCreated.getId() + "/schedule")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(schedule)))
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails("mocked-user")
  void WorkoutModelController_removeScheduleFromWorkoutModel_ReturnsWorkoutModelWithoutSchedule() throws Exception {
    WorkoutModelCreateDTO workoutModel = new WorkoutModelCreateDTO(
        "KUNG FU",
        "Martial Arts",
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

    WorkoutModel workoutModelCreated = workoutModelRepository.findByName("KUNG FU").orElse(null);
    assert workoutModelCreated != null;

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

    mockMvc.perform(post("/api/workout-model/" + workoutModelCreated.getId() + "/schedule")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(schedule)))
        .andExpect(status().isOk());

    mockMvc.perform(get("/api/workout-model/" + workoutModelCreated.getId()))
        .andExpect(status().isOk());

    mockMvc.perform(delete("/api/workout-model/schedule/1"))
        .andExpect(status().isOk());

    mockMvc.perform(get("/api/workout-model/" + workoutModelCreated.getId()))
        .andExpect(status().isOk());
  }
}
