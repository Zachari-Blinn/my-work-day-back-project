package com.blinnproject.myworkdayback.controllers;

import com.blinnproject.myworkdayback.AbstractIntegrationTest;
import com.blinnproject.myworkdayback.model.dto.ScheduleCreateDTO;
import com.blinnproject.myworkdayback.model.dto.WorkoutExerciseCreateDTO;
import com.blinnproject.myworkdayback.model.dto.WorkoutExerciseUpdateDTO;
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
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkoutModelControllerTest extends AbstractIntegrationTest {

  private static final String API_PATH = "/api/workout-model";

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
  @Autowired
  private ModelMapper modelMapper;

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
  @DisplayName("Test connection to database")
  void testConnectionToDatabase() {
    Assertions.assertNotNull(userRepository);
    Assertions.assertNotNull(workoutModelRepository);
    Assertions.assertNotNull(exerciseRepository);
  }

  @Test
  @Order(value = 2)
  @WithUserDetails("mocked-user")
  @DisplayName("Create a workout model")
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
        .contentType(MediaType.APPLICATION_JSON)
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
  @Order(value = 3)
  @WithUserDetails("mocked-user")
  @DisplayName("Get a workout model by id")
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

    mockMvc.perform(post(API_PATH)
        .contentType(MediaType.APPLICATION_JSON)
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

    mockMvc.perform(get(API_PATH + "/{id}", workoutModelCreated.getId()))
        .andExpect(status().isOk());
  }

  @Test
  @Order(value = 4)
  @WithUserDetails("mocked-user")
  @DisplayName("Get a workout model by id when not exists")
  void WorkoutModelController_GetWorkoutModelById_WhenNotExists_ReturnsNotFound() throws Exception {
    mockMvc.perform(get(API_PATH + "/{id}", "42"))
        .andExpect(status().isNotFound());
  }

  @Test
  @Order(value = 5)
  @WithUserDetails("mocked-user")
  @DisplayName("Add exercise to workout model without sets")
  void WorkoutModelController_addExerciseToWorkoutModelWithoutSets_ReturnsWorkoutModelWithExercise() throws Exception {
    Exercise exercise = new Exercise();
    exercise.setName("Punching");
    exercise.setCreatedBy(user.getId());
    Exercise savedExercise = exerciseRepository.saveAndFlush(exercise);

    WorkoutModelCreateDTO workoutModel = new WorkoutModelCreateDTO(
        "Trick War",
        "Trick War",
        String.valueOf(ESport.MMA),
        true,
        true,
        "icon_dumbbell",
        "#0072db"
    );

    MvcResult workoutModelResult = mockMvc.perform(post(API_PATH)
        .contentType(MediaType.APPLICATION_JSON)
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

    mockMvc.perform(post(API_PATH + "/" + workoutModelId + "/exercise/" + savedExercise.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(workoutExercise)))
        .andExpect(status().isOk());
  }

  @Test
  @Order(value = 6)
  @WithUserDetails("mocked-user")
  @DisplayName("Add exercise to workout model with sets")
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

    MvcResult workoutModelResult = mockMvc.perform(post(API_PATH)
        .contentType(MediaType.APPLICATION_JSON)
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
        "New Punching",
        2,
        new ArrayList<>(Arrays.asList(workoutSet1, workoutSet2))
    );

    mockMvc.perform(post(API_PATH + "/" + workoutModelId + "/exercise/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(workoutExercise)))
        .andExpect(status().isOk());
  }

  @Test
  @Order(value = 7)
  @WithUserDetails("mocked-user")
  @DisplayName("Remove exercise from workout model")
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

    MvcResult workoutModelResult = mockMvc.perform(post(API_PATH)
        .contentType(MediaType.APPLICATION_JSON)
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

    MvcResult workoutExerciseResult = mockMvc.perform(post(API_PATH + "/" + workoutModelId + "/exercise/" + savedExercise.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(workoutExercise)))
        .andExpect(status().isOk())
      .andReturn();

    mockMvc.perform(get(API_PATH + "/" + workoutModelId))
        .andExpect(status().isOk())
      ;

    String response = workoutExerciseResult.getResponse().getContentAsString();
    JsonNode jsonNode = objectMapper.readTree(response);
    long workoutExerciseId = jsonNode.at("/data/id").asLong();

    mockMvc.perform(delete(API_PATH + "/exercise/" + workoutExerciseId))
        .andExpect(status().isOk());

    mockMvc.perform(get(API_PATH + "/" + workoutModelId))
        .andExpect(status().isOk());
  }

  @Test
  @Order(value = 8)
  @WithUserDetails("mocked-user")
  @DisplayName("Delete workout model")
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

    MvcResult workoutModelResult = mockMvc.perform(post(API_PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(workoutModel)))
        .andExpect(status().isCreated())
      .andReturn();
    long workoutModelId = objectMapper.readTree(workoutModelResult.getResponse().getContentAsString()).at("/data/id").asLong();

    mockMvc.perform(delete(API_PATH + "/{id}", workoutModelId))
        .andExpect(status().isNoContent());
  }

  @Test
  @Order(value = 9)
  @WithUserDetails("mocked-user")
  void WorkoutModelController_deleteWorkoutModel_WhenNotExists_ReturnsNotFound() throws Exception {
    mockMvc.perform(delete(API_PATH + "/{id}", "42"))
        .andExpect(status().isNotFound());
  }

  @Test
  @Order(value = 10)
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

    mockMvc.perform(post(API_PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(workoutModel1)))
        .andExpect(status().isCreated());

    mockMvc.perform(post(API_PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(workoutModel2)))
        .andExpect(status().isCreated());

    mockMvc.perform(get(API_PATH))
        .andExpect(status().isOk());
  }

  @Test
  @Order(value = 11)
  @WithUserDetails("mocked-user")
  @DisplayName("Update workout model")
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

    MvcResult workoutModelResult = mockMvc.perform(post(API_PATH)
        .contentType(MediaType.APPLICATION_JSON)
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

    mockMvc.perform(patch(API_PATH + "/" + workoutModelId)
        .contentType(MediaType.APPLICATION_JSON)
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
  @Order(value = 12)
  @WithUserDetails("mocked-user")
  @DisplayName("Update workout model when not exists")
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

    mockMvc.perform(patch(API_PATH + "/{id}", "42")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updatedWorkoutModel)))
        .andExpect(status().isNotFound());
  }

  @Test
  @Order(value = 13)
  @WithUserDetails("mocked-user")
  @DisplayName("Find all exercises")
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

    MvcResult workoutModelResult = mockMvc.perform(post(API_PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(workoutModel)))
        .andExpect(status().isCreated())
      .andReturn();
    long workoutModelId = objectMapper.readTree(workoutModelResult.getResponse().getContentAsString()).at("/data/id").asLong();

    mockMvc.perform(get(API_PATH + "/" + workoutModelId + "/exercises"))
        .andExpect(status().isOk());
  }

  @Test
  @Order(value = 14)
  @WithUserDetails("mocked-user")
  @DisplayName("Find all exercises when not exists")
  void WorkoutModelController_findAllExercises_WhenNotExists_ReturnsNotNoneExercise() throws Exception {
    mockMvc.perform(get(API_PATH + "/{id}/exercises", "42"))
        .andExpect(status().isOk());
  }

  @Test
  @Order(value = 15)
  @WithUserDetails("mocked-user")
  @DisplayName("Add schedule to workout model")
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

    MvcResult workoutModelResult = mockMvc.perform(post(API_PATH)
        .contentType(MediaType.APPLICATION_JSON)
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

    mockMvc.perform(post(API_PATH + "/" + workoutModelId + "/schedule")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(schedule)))
        .andExpect(status().isOk());
  }

  @Test
  @Order(value = 16)
  @WithUserDetails("mocked-user")
  @DisplayName("Add schedule to workout model when not exists")
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

    MvcResult workoutModelResult = mockMvc.perform(post(API_PATH)
        .contentType(MediaType.APPLICATION_JSON)
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

    mockMvc.perform(post(API_PATH + "/" + workoutModelId + "/schedule")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(schedule)))
        .andExpect(status().isOk());

    mockMvc.perform(get(API_PATH + "/{id}", workoutModelId))
        .andExpect(status().isOk());

    mockMvc.perform(delete(API_PATH + "/schedule/1"))
        .andExpect(status().isOk());

    mockMvc.perform(get(API_PATH + "/{id}", workoutModelId))
        .andExpect(status().isOk());
  }

  @Test
  @Order(value = 17)
  @WithUserDetails("mocked-user")
  @DisplayName("Update schedule of workout model")
  void WorkoutModelController_updateScheduleOfWorkoutModel_ReturnsUpdatedSchedule() throws Exception {
    WorkoutModelCreateDTO workoutModel = new WorkoutModelCreateDTO(
        "Kung Fu",
        "Martial Arts",
        String.valueOf(ESport.MMA),
        true,
        true,
        "icon_dumbbell",
        "#0072db"
    );

    MvcResult workoutModelResult = mockMvc.perform(post(API_PATH)
        .contentType(MediaType.APPLICATION_JSON)
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
      true,
      true,
      true,
      true,
      true,
      true
    );

    MvcResult scheduleAddedResult = mockMvc.perform(post(API_PATH + "/" + workoutModelId + "/schedule")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(schedule)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.monday").value(true))
      .andExpect(jsonPath("$.data.friday").value(true))
      .andExpect(jsonPath("$.data.frequency").value(EFrequency.WEEKLY.toString()))
      .andReturn();

    String response = scheduleAddedResult.getResponse().getContentAsString();
    JsonNode jsonNode = objectMapper.readTree(response);
    long scheduleId = jsonNode.at("/data/id").asLong();

    ScheduleCreateDTO updatedSchedule = new ScheduleCreateDTO(
        LocalDate.parse("2023-12-01"),
        LocalDate.parse("2023-12-01"),
        LocalTime.parse("06:00:00"),
        LocalTime.parse("07:00:00"),
        EFrequency.BIWEEKLY,
        false,
      false,
      false,
      false,
      false,
      false,
      false
    );

    mockMvc.perform(patch(API_PATH + "/schedule/" + scheduleId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updatedSchedule)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.id").value(scheduleId))
        .andExpect(jsonPath("$.data.monday").value(false))
        .andExpect(jsonPath("$.data.friday").value(false))
        .andExpect(jsonPath("$.data.frequency").value(EFrequency.BIWEEKLY.toString()));
  }

  @Test
  @Order(value = 18)
  @WithUserDetails("mocked-user")
  @DisplayName("Update exercise of workout model")
  void WorkoutModelController_updateExerciseOfWorkoutModel_ReturnsUpdatedExercise() throws Exception {
    WorkoutModelCreateDTO workoutModel = new WorkoutModelCreateDTO(
      "New Rosurf",
      "Rosurf",
      String.valueOf(ESport.MMA),
      true,
      true,
      "icon_dumbbell",
      "#0072db"
    );

    MvcResult workoutModelResult = mockMvc.perform(post(API_PATH)
        .contentType(MediaType.APPLICATION_JSON)
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
      "New Rosurf exercise",
      null
    );
    Exercise savedExercise = exerciseRepository.saveAndFlush(exercise);

    MvcResult workoutExerciseAddedResult = mockMvc.perform(post(API_PATH + "/" + workoutModelId + "/exercise/" + savedExercise.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(workoutExercise)))
      .andExpect(status().isOk())
      .andReturn();
    long workoutExerciseId = objectMapper.readTree(workoutExerciseAddedResult.getResponse().getContentAsString()).at("/data/id").asLong();

    WorkoutExerciseUpdateDTO updatedWorkoutExercise = new WorkoutExerciseUpdateDTO (
        2,
        "Punching",
        2
    );

    mockMvc.perform(patch(API_PATH + "/exercise/" + workoutExerciseId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updatedWorkoutExercise)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.id").value(workoutExerciseId))
        .andExpect(jsonPath("$.data.positionIndex").value(2))
        .andExpect(jsonPath("$.data.notes").value("Punching"))
        .andExpect(jsonPath("$.data.numberOfWarmUpSets").value(2));
  }
}
