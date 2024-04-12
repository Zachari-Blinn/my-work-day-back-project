package com.blinnproject.myworkdayback.controllers;

import com.blinnproject.myworkdayback.AbstractIntegrationTest;
import com.blinnproject.myworkdayback.model.entity.User;
import com.blinnproject.myworkdayback.model.enums.EGender;
import com.blinnproject.myworkdayback.model.request.UpdateUserPasswordDTO;
import com.blinnproject.myworkdayback.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest extends AbstractIntegrationTest {

  private static final String API_PATH = "/api/user";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  private User user1;
  private User user2;

  @Autowired
  private PasswordEncoder encoder;

  @BeforeAll
  void beforeAllTests() {
    createMockedUser();
  }

  void createMockedUser() {
    user1 = new User();

    user1.setUsername("mocked-user-to-update");
    user1.setPassword("Toto@123*");
    user1.setEmail("mocked-user-to-update@email.fr");
    user1.setGender(EGender.MAN);
    user1 = userRepository.save(user1);

    user2 = new User();
    user2.setUsername("mocked-update-pwd-user");
    String passwordEncoded = encoder.encode("Toto@123*");
    user2.setPassword(passwordEncoded);
    user2.setEmail("mocked-update-pwd-user@email.fr");
    user2.setGender(EGender.MAN);
    user2 = userRepository.save(user2);

  }

  @Test
  @Order(value = 1)
  @DisplayName("Test Connection to Database")
  void testConnectionToDatabase() {
    Assertions.assertNotNull(userRepository);
  }

  @Test
  @Order(value = 2)
  @WithUserDetails("mocked-user-to-update")
  @DisplayName("Update user profile")
  void updateUserProfile() throws Exception {
    User updatedUser = new User();
    updatedUser.setUsername("new username");
    updatedUser.setEmail("new_username@fake-email.fr");

    mockMvc.perform(patch(API_PATH + "/profile")
      .contentType("application/json")
      .content(objectMapper.writeValueAsString(updatedUser))
    )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.username").value(updatedUser.getUsername()))
      .andExpect(jsonPath("$.data.email").value(updatedUser.getEmail()));
  }

  @Test
  @Order(value = 3)
  @WithUserDetails("mocked-update-pwd-user")
  @DisplayName("Update user password")
  void updateUserPassword() throws Exception {
    UpdateUserPasswordDTO updatedUser = new UpdateUserPasswordDTO();
    updatedUser.setOldPassword("Toto@123*");
    updatedUser.setNewPassword("newPassword@2021*");

    mockMvc.perform(patch(API_PATH + "/password")
      .contentType("application/json")
      .content(objectMapper.writeValueAsString(updatedUser))
    )
      .andExpect(status().isOk());
  }
}
