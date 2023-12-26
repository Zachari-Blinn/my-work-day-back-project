package com.blinnproject.myworkdayback.controllers;

import com.blinnproject.myworkdayback.model.*;
import com.blinnproject.myworkdayback.payload.request.AddExerciseRequest;
import com.blinnproject.myworkdayback.repository.ExerciseRepository;
import com.blinnproject.myworkdayback.repository.TrainingRepository;
import com.blinnproject.myworkdayback.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

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
    Training training = new Training();
    training.setName("MMA");
    training.setIconName("icon_dumbbell");
    training.setIconHexadecimalColor("#0072db");
    ArrayList<DayOfWeek> days = new ArrayList<>();
    days.add(DayOfWeek.TUESDAY);
    days.add(DayOfWeek.THURSDAY);
    days.add(DayOfWeek.WEDNESDAY);
    training.setTrainingDays(days);

    mockMvc.perform(post("/api/training")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(training)))
        .andExpect(status().isCreated());

    Training createdTraining = trainingRepository.findByName("MMA").orElse(null);
    assert(Objects.requireNonNull(createdTraining).getName()).equals("MMA");
    assert(Objects.requireNonNull(createdTraining).getIconName()).equals("icon_dumbbell");
    assert(Objects.requireNonNull(createdTraining).getIconHexadecimalColor()).equals("#0072db");
    assert(Objects.requireNonNull(createdTraining).getTrainingDays()).equals(days);
  }

  @Test
  @WithUserDetails("mocked-user")
  void TrainingController_AddExercise_ipsum() throws Exception {
    Training training = new Training();
    training.setName("Crossfit");
    training.setIconName("icon_dumbbell");
    training.setIconHexadecimalColor("#0072db");
    ArrayList<DayOfWeek> days = new ArrayList<>();
    days.add(DayOfWeek.TUESDAY);
    days.add(DayOfWeek.THURSDAY);
    days.add(DayOfWeek.WEDNESDAY);
    training.setTrainingDays(days);

    mockMvc.perform(post("/api/training")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(training)))
        .andExpect(status().isCreated());

    Training createdTraining = trainingRepository.findByName("Crossfit").orElse(null);
    assert createdTraining != null;

    Exercise newExercise = new Exercise();
    newExercise.setName("New exercise");
    newExercise.setMusclesUsed(new HashSet<>(Arrays.asList(EMuscle.QUADRICEPS, EMuscle.DELTOID)));
    Exercise createdExercise = ExerciseRepository.save(newExercise);

    AddExerciseRequest addExerciseRequest = new AddExerciseRequest();
    addExerciseRequest.setExerciseId(createdExercise.getId());
    addExerciseRequest.setNumberOfWarmUpSeries(2);
    addExerciseRequest.setNotes("Lorem ipsum dollores");

    ArrayList<Series> series = new ArrayList<>();
    Series series1 = new Series();
    series1.setRepsCount(8);
    series1.setWeight(50);
    series1.setRestTime("60");
    series.add(series1);
    addExerciseRequest.setSeries(series);

    mockMvc.perform(post("/api/training/{trainingId}/add-exercise", createdTraining.getId())
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(addExerciseRequest)))
        .andExpect(status().isOk());
  }

}
