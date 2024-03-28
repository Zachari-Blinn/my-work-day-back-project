package com.blinnproject.myworkdayback.controllers;

import com.blinnproject.myworkdayback.model.entity.Exercise;
import com.blinnproject.myworkdayback.model.entity.WorkoutSet;
import com.blinnproject.myworkdayback.model.entity.WorkoutSession;
import com.blinnproject.myworkdayback.model.entity.User;
import com.blinnproject.myworkdayback.model.enums.EDayOfWeek;
import com.blinnproject.myworkdayback.model.enums.EGender;
import com.blinnproject.myworkdayback.model.enums.EMuscle;
import com.blinnproject.myworkdayback.payload.request.AddExerciseRequest;
import com.blinnproject.myworkdayback.repository.ExerciseRepository;
import com.blinnproject.myworkdayback.repository.TrainingRepository;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TrainingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TrainingRepository trainingRepository;

  @Autowired
  private ExerciseRepository ExerciseRepository;

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
  @WithMockUser()
  void TrainingController_Create_ReturnSavedTraining() throws Exception {
    WorkoutSession training = new WorkoutSession();
    training.setName("MMA");
    training.setIconName("icon_dumbbell");
    training.setIconHexadecimalColor("#0072db");
    ArrayList<EDayOfWeek> days = new ArrayList<>();
    days.add(EDayOfWeek.TUESDAY);
    days.add(EDayOfWeek.THURSDAY);
    days.add(EDayOfWeek.WEDNESDAY);
    training.setTrainingDays(days);

    mockMvc.perform(post("/api/training")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(training)))
        .andExpect(status().isCreated());

    WorkoutSession createdTraining = trainingRepository.findByName("MMA").orElse(null);
    assert(Objects.requireNonNull(createdTraining).getName()).equals("MMA");
    assert(Objects.requireNonNull(createdTraining).getIconName()).equals("icon_dumbbell");
    assert(Objects.requireNonNull(createdTraining).getIconHexadecimalColor()).equals("#0072db");
    assert(Objects.requireNonNull(createdTraining).getTrainingDays()).equals(days);
  }

  @Test
  @WithUserDetails("mocked-user")
  void TrainingController_AddExercise_ReturnOk() throws Exception {
    WorkoutSession training = new WorkoutSession();
    training.setName("Crossfit");
    training.setIconName("icon_dumbbell");
    training.setIconHexadecimalColor("#0072db");
    ArrayList<EDayOfWeek> days = new ArrayList<>();
    days.add(EDayOfWeek.TUESDAY);
    days.add(EDayOfWeek.THURSDAY);
    days.add(EDayOfWeek.WEDNESDAY);
    training.setTrainingDays(days);

    mockMvc.perform(post("/api/training")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(training)))
        .andExpect(status().isCreated());

    WorkoutSession createdTraining = trainingRepository.findByName("Crossfit").orElse(null);
    assert createdTraining != null;

    Exercise newExercise = new Exercise();
    newExercise.setName("New exercise");
    newExercise.setMusclesUsed(new HashSet<>(Arrays.asList(EMuscle.QUADRICEPS, EMuscle.DELTOID)));
    Exercise createdExercise = ExerciseRepository.save(newExercise);

    AddExerciseRequest addExerciseRequest = new AddExerciseRequest();
    addExerciseRequest.setNumberOfWarmUpSeries(2);
    addExerciseRequest.setNotes("Lorem ipsum dollores");

    ArrayList<WorkoutSet> series = new ArrayList<>();
    WorkoutSet series1 = new WorkoutSet();
    series1.setRepsCount(8);
    series1.setWeight(50);
    series1.setRestTime("60");
    series.add(series1);
    addExerciseRequest.setSeries(series);

    mockMvc.perform(post("/api/training/{trainingId}/exercise/{exerciseId}", createdTraining.getId(), createdExercise.getId())
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(addExerciseRequest)))
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails("mocked-user")
  void TrainingController_GetTrainingById_ReturnOk() throws Exception {
    WorkoutSession training = new WorkoutSession();
    training.setName("Séance de Force");
    training.setIconName("icon_dumbbell");
    training.setIconHexadecimalColor("#0072db");
    ArrayList<EDayOfWeek> days = new ArrayList<>();
    days.add(EDayOfWeek.TUESDAY);
    days.add(EDayOfWeek.THURSDAY);
    days.add(EDayOfWeek.WEDNESDAY);
    training.setTrainingDays(days);

    mockMvc.perform(post("/api/training")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(training)))
        .andExpect(status().isCreated());

    WorkoutSession createdTraining = trainingRepository.findByName("Séance de Force").orElse(null);
    assert createdTraining != null;

    mockMvc.perform(get("/api/training/{id}", createdTraining.getId())
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(createdTraining)))
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails("mocked-user")
  void TrainingController_AddExerciseToTraining_ReturnOk() throws Exception {
    WorkoutSession training = new WorkoutSession();
    training.setName("Entraînement Équilibré");
    training.setIconName("icon_dumbbell");
    training.setIconHexadecimalColor("#0072db");
    ArrayList<EDayOfWeek> days = new ArrayList<>();
    days.add(EDayOfWeek.TUESDAY);
    days.add(EDayOfWeek.THURSDAY);
    days.add(EDayOfWeek.WEDNESDAY);
    training.setTrainingDays(days);

    mockMvc.perform(post("/api/training")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(training)))
        .andExpect(status().isCreated());

    WorkoutSession createdTraining = trainingRepository.findByName("Entraînement Équilibré").orElse(null);
    assert createdTraining != null;

    Exercise newExercise = new Exercise();
    newExercise.setName("New exercise");
    newExercise.setMusclesUsed(new HashSet<>(Arrays.asList(EMuscle.QUADRICEPS, EMuscle.DELTOID)));
    Exercise createdExercise = ExerciseRepository.save(newExercise);

    AddExerciseRequest addExerciseRequest = new AddExerciseRequest();
    addExerciseRequest.setNumberOfWarmUpSeries(2);
    addExerciseRequest.setNotes("Lorem ipsum dollores");

    ArrayList<WorkoutSet> series = new ArrayList<>();
    WorkoutSet series1 = new WorkoutSet();
    series1.setRepsCount(8);
    series1.setWeight(50);
    series1.setRestTime("60");
    series.add(series1);
    addExerciseRequest.setSeries(series);

    mockMvc.perform(post("/api/training/{trainingId}/exercise/{exerciseId}", createdTraining.getId(), createdExercise.getId())
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(addExerciseRequest)))
        .andExpect(status().isOk());
  }
}
