package com.blinnproject.myworkdayback.controllers;

import com.blinnproject.myworkdayback.model.entity.User;
import com.blinnproject.myworkdayback.model.enums.EGender;
import com.blinnproject.myworkdayback.repository.WorkoutSessionRepository;
import com.blinnproject.myworkdayback.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TrainingSessionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private WorkoutSessionRepository trainingRepository;

  @Autowired
  private com.blinnproject.myworkdayback.repository.ExerciseRepository ExerciseRepository;

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

//  @Test
//  @WithUserDetails("mocked-user")
//  void TrainingSessionController_ValidateTrainingSession_ReturnOk() throws Exception {
//    WorkoutSession training = new WorkoutSession();
//    training.setName("Renforcement Musculaire Global");
//    training.setIconName("icon_dumbbell");
//    training.setIconHexadecimalColor("#0072db");
//    ArrayList<EDayOfWeek> days = new ArrayList<>();
//    days.add(EDayOfWeek.TUESDAY);
//    days.add(EDayOfWeek.THURSDAY);
//    days.add(EDayOfWeek.WEDNESDAY);
//    training.setTrainingDays(days);
//
//    mockMvc.perform(post("/api/training")
//        .contentType("application/json")
//        .content(objectMapper.writeValueAsString(training)))
//      .andExpect(status().isCreated());
//
//    WorkoutSession createdTraining = trainingRepository.findByName("Renforcement Musculaire Global").orElse(null);
//    assert createdTraining != null;
//
//    Exercise newExercise = new Exercise();
//    newExercise.setName("New exercise");
//    newExercise.setMusclesUsed(new HashSet<>(Arrays.asList(EMuscle.QUADRICEPS, EMuscle.DELTOID)));
//    Exercise createdExercise = ExerciseRepository.save(newExercise);
//
//    AddExerciseRequest addExerciseRequest = new AddExerciseRequest();
//    addExerciseRequest.setNumberOfWarmUpSeries(2);
//    addExerciseRequest.setNotes("Lorem ipsum dollores");
//
//    ArrayList<WorkoutSet> series = new ArrayList<>();
//    WorkoutSet series1 = new WorkoutSet();
//    series1.setRepsCount(8);
//    series1.setWeight(50);
//    series1.setRestTime("60");
//    series.add(series1);
//    addExerciseRequest.setSeries(series);
//
//    mockMvc.perform(post("/api/training/{trainingId}/exercise/{exerciseId}", createdTraining.getId(), createdExercise.getId())
//        .contentType("application/json")
//        .content(objectMapper.writeValueAsString(addExerciseRequest)))
//      .andExpect(status().isOk());
//
//    mockMvc.perform(post("/api/training-session/{trainingId}/validate/{trainingDate}", createdTraining.getId(), "2024-01-03")
//        .contentType("application/json")
//        .content(objectMapper.writeValueAsString(createdTraining)))
//      .andExpect(status().isOk());
//  }
//
//  @Test
//  @WithUserDetails("mocked-user")
//  void TrainingSessionController_ValidateTrainingSession_WithInvalidDayOfWeek_Return4xxClientError() throws Exception {
//    WorkoutSession training = new WorkoutSession();
//    training.setName("Séance Mixte");
//    training.setIconName("icon_dumbbell");
//    training.setIconHexadecimalColor("#0072db");
//    ArrayList<EDayOfWeek> days = new ArrayList<>();
//    days.add(EDayOfWeek.TUESDAY);
//    days.add(EDayOfWeek.THURSDAY);
//    days.add(EDayOfWeek.WEDNESDAY);
//    training.setTrainingDays(days);
//
//    mockMvc.perform(post("/api/training")
//        .contentType("application/json")
//        .content(objectMapper.writeValueAsString(training)))
//      .andExpect(status().isCreated());
//
//    WorkoutSession createdTraining = trainingRepository.findByName("Séance Mixte").orElse(null);
//    assert createdTraining != null;
//
//    Exercise newExercise = new Exercise();
//    newExercise.setName("New exercise");
//    newExercise.setMusclesUsed(new HashSet<>(Arrays.asList(EMuscle.QUADRICEPS, EMuscle.DELTOID)));
//    Exercise createdExercise = ExerciseRepository.save(newExercise);
//
//    AddExerciseRequest addExerciseRequest = new AddExerciseRequest();
//    addExerciseRequest.setNumberOfWarmUpSeries(2);
//    addExerciseRequest.setNotes("Lorem ipsum dollores");
//
//    ArrayList<WorkoutSet> series = new ArrayList<>();
//    WorkoutSet series1 = new WorkoutSet();
//    series1.setRepsCount(8);
//    series1.setWeight(50);
//    series1.setRestTime("60");
//    series.add(series1);
//    addExerciseRequest.setSeries(series);
//
//    mockMvc.perform(post("/api/training/{trainingId}/exercise/{exerciseId}", createdTraining.getId(), createdExercise.getId())
//        .contentType("application/json")
//        .content(objectMapper.writeValueAsString(addExerciseRequest)))
//      .andExpect(status().isOk());
//
//    mockMvc.perform(post("/api/training-session/{trainingId}/validate/{trainingDate}", createdTraining.getId(), "2024-01-05")
//        .contentType("application/json")
//        .content(objectMapper.writeValueAsString(createdTraining)))
//      .andExpect(status().is4xxClientError());
//  }
//
//  @Test
//  @WithUserDetails("mocked-user")
//  void TrainingSessionController_PatchTrainingSession_ReturnOk() throws Exception {
//
//  }
}
