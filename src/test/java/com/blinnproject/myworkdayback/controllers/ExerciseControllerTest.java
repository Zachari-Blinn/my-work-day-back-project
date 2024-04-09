package com.blinnproject.myworkdayback.controllers;

import com.blinnproject.myworkdayback.AbstractIntegrationTest;
import com.blinnproject.myworkdayback.model.dto.ExerciseCreateDTO;
import com.blinnproject.myworkdayback.model.entity.User;
import com.blinnproject.myworkdayback.model.enums.EGender;
import com.blinnproject.myworkdayback.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExerciseControllerTest extends AbstractIntegrationTest {

  private static final String API_PATH = "/api/exercise";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

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

  @Test
  @Order(value = 1)
  @DisplayName("Test Connection to Database")
  void testConnectionToDatabase() {
    Assertions.assertNotNull(userRepository);
  }

  @Test
  @Order(value = 2)
  @WithUserDetails("mocked-user")
  @DisplayName("Create Exercise")
  void ExerciseController_Create_ReturnSavedExercise() throws Exception {
    ExerciseCreateDTO exercise = new ExerciseCreateDTO(
        "mocked-exercise",
        null
    );

    mockMvc.perform(post("/api/exercise")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(exercise)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.name").value("mocked-exercise"))
        .andExpect(jsonPath("$.data.createdBy").value(user.getId()));
  }

  @Test
  @Order(value = 3)
  @WithUserDetails("mocked-user")
  @DisplayName("Create Exercise with null values returns BadRequest")
  void ExerciseController_Create_ReturnBadRequest() throws Exception {
    ExerciseCreateDTO exercise = new ExerciseCreateDTO(
        null,
        null
    );

    mockMvc.perform(post(API_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(exercise)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(value = 4)
  @WithUserDetails("mocked-user")
  @DisplayName("Find Exercise by Id")
  void ExerciseController_findById_ReturnExercise() throws Exception {
    ExerciseCreateDTO exercise = new ExerciseCreateDTO(
        "mocked-exercise",
        null
    );

    MvcResult postResult = mockMvc.perform(post(API_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(exercise)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.name").value("mocked-exercise"))
        .andExpect(jsonPath("$.data.createdBy").value(user.getId()))
        .andExpect(jsonPath("$.data.id").exists())
        .andReturn();

    String response = postResult.getResponse().getContentAsString();
    JsonNode jsonNode = objectMapper.readTree(response);
    Long exerciseId = jsonNode.at("/data/id").asLong();

    mockMvc.perform(get(API_PATH + "/{id}", exerciseId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.name").value("mocked-exercise"))
        .andExpect(jsonPath("$.data.createdBy").value(user.getId()));
  }

  @Test
  @Order(value = 5)
  @WithUserDetails("mocked-user")
  @DisplayName("Find Exercise with wrong id")
  void ExerciseController_findById_ReturnNotFound() throws Exception {
    mockMvc.perform(get(API_PATH + "/{id}", 999))
        .andExpect(status().isNotFound());
  }

  @Test
  @Order(value = 6)
  @WithUserDetails("mocked-user")
  @DisplayName("Find All Exercises")
  void ExerciseController_findAll_ReturnAllExercises() throws Exception {
    ExerciseCreateDTO exercise = new ExerciseCreateDTO(
        "mocked-exercise",
        null
    );

    mockMvc.perform(post(API_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(exercise)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.name").value("mocked-exercise"))
        .andExpect(jsonPath("$.data.createdBy").value(user.getId()));

    mockMvc.perform(get(API_PATH))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[*].name", hasItem("mocked-exercise")));
  }

  @Test
  @Order(value = 7)
  @WithUserDetails("mocked-user")
  @DisplayName("Update Exercise")
  void ExerciseController_update_ReturnUpdatedExercise() throws Exception {
    ExerciseCreateDTO exercise = new ExerciseCreateDTO(
        "mocked-exercise",
        null
    );

    MvcResult postResult = mockMvc.perform(post(API_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(exercise)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.name").value("mocked-exercise"))
        .andExpect(jsonPath("$.data.createdBy").value(user.getId()))
        .andExpect(jsonPath("$.data.id").exists())
        .andReturn();

    String response = postResult.getResponse().getContentAsString();
    JsonNode jsonNode = objectMapper.readTree(response);
    Long exerciseId = jsonNode.at("/data/id").asLong();

    ExerciseCreateDTO updatedExercise = new ExerciseCreateDTO(
        "updated-mocked-exercise",
        null
    );

    mockMvc.perform(put(API_PATH + "/{id}", exerciseId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedExercise)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.name").value("updated-mocked-exercise"))
        .andExpect(jsonPath("$.data.createdBy").value(user.getId()));
  }

  @Test
  @Order(value = 8)
  @WithUserDetails("mocked-user")
  @DisplayName("Update Exercise with wrong id")
  void ExerciseController_update_ReturnNotFound() throws Exception {
    ExerciseCreateDTO updatedExercise = new ExerciseCreateDTO(
        "updated-mocked-exercise",
        null
    );

    mockMvc.perform(put(API_PATH + "/{id}", 999)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedExercise)))
        .andExpect(status().isNotFound());
  }

  @Test
  @Order(value = 9)
  @WithUserDetails("mocked-user")
  @DisplayName("Delete Exercise")
  void ExerciseController_delete_ReturnNoContent() throws Exception {
    ExerciseCreateDTO exercise = new ExerciseCreateDTO(
        "mocked-exercise",
        null
    );

    MvcResult postResult = mockMvc.perform(post(API_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(exercise)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.name").value("mocked-exercise"))
        .andExpect(jsonPath("$.data.createdBy").value(user.getId()))
        .andExpect(jsonPath("$.data.id").exists())
        .andReturn();

    String response = postResult.getResponse().getContentAsString();
    JsonNode jsonNode = objectMapper.readTree(response);
    Long exerciseId = jsonNode.at("/data/id").asLong();

    mockMvc.perform(delete(API_PATH + "/{id}", exerciseId))
        .andExpect(status().isNoContent());
  }

  @Test
  @Order(value = 10)
  @WithUserDetails("mocked-user")
  @DisplayName("Delete Exercise with wrong id")
  void ExerciseController_delete_ReturnNotFound() throws Exception {
    mockMvc.perform(delete(API_PATH + "/{id}", 999))
        .andExpect(status().isNotFound());
  }
}
