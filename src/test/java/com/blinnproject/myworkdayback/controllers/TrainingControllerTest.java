package com.blinnproject.myworkdayback.controllers;

import com.blinnproject.myworkdayback.model.Training;
import com.blinnproject.myworkdayback.repository.TrainingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TrainingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TrainingRepository trainingRepository;

  @Test
  @WithMockUser
  public void TrainingController_Create_ReturnSavedTraining() throws Exception {
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
}
