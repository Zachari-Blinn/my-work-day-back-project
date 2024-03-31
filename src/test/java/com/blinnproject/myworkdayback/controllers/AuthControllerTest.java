package com.blinnproject.myworkdayback.controllers;

import com.blinnproject.myworkdayback.model.request.LoginRequest;
import com.blinnproject.myworkdayback.model.request.SignupRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void AuthController_Register_ReturnSavedUser() throws Exception {
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setUsername("mocked-user");
    signupRequest.setPassword("Toto@123*");
    signupRequest.setEmail("mocked-user@email.fr");

    mockMvc.perform(post("/api/auth/signup")
      .contentType("application/json")
      .content(objectMapper.writeValueAsString(signupRequest)))
      .andExpect(status().isOk());
  }

  @Test
  void AuthController_RegisterWithBlankValues_ReturnError() throws Exception {
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setUsername("");
    signupRequest.setPassword("");
    signupRequest.setEmail("");

    ResultActions result = mockMvc.perform(post("/api/auth/signup")
      .contentType("application/json")
      .content(objectMapper.writeValueAsString(signupRequest)))
      .andExpect(status().isBadRequest());

    String responseContent = result.andReturn().getResponse().getContentAsString();

    assertTrue(responseContent.contains("\"password\""));
    assertTrue(responseContent.contains("\"email\""));
    assertTrue(responseContent.contains("\"username\""));
  }

  @Test
  void AuthController_Login_ReturnUserAndTokenResponse() throws Exception {
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setUsername("mocked-user");
    signupRequest.setPassword("Toto@123*");
    signupRequest.setEmail("mocked-user@email.fr");

    mockMvc.perform(post("/api/auth/signup")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(signupRequest)))
        .andExpect(status().isOk());

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUsername("mocked-user");
    loginRequest.setPassword("Toto@123*");

    mockMvc.perform(post("/api/auth/signin")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.accessToken").exists())
        .andExpect(jsonPath("$.data.refreshToken").exists())
        .andExpect(jsonPath("$.data.id").exists())
        .andExpect(jsonPath("$.data.username").exists())
        .andExpect(jsonPath("$.data.email").exists())
        .andExpect(jsonPath("$.data.roles").exists());
  }

  @Test
  void AuthController_LoginWithWrongCredentials_ReturnUnauthorizedError() throws Exception {
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setUsername("mocked-user");
    signupRequest.setPassword("Toto@123*");
    signupRequest.setEmail("mocked-user@email.fr");

    mockMvc.perform(post("/api/auth/signup")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(signupRequest)))
        .andExpect(status().isOk());

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUsername("mocked-user");
    loginRequest.setPassword("Wrong-password@123*");

    mockMvc.perform(post("/api/auth/signin")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void AuthController_LoginWithBlankValues_ReturnError() throws Exception {
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUsername("");
    loginRequest.setPassword("");

    ResultActions result = mockMvc.perform(post("/api/auth/signin")
      .contentType("application/json")
      .content(objectMapper.writeValueAsString(loginRequest)))
      .andExpect(status().isBadRequest());

    String responseContent = result.andReturn().getResponse().getContentAsString();

    assertTrue(responseContent.contains("\"password\""));
    assertTrue(responseContent.contains("\"username\""));
  }

//  @Test
//  void AuthController_RefreshToken_ReturnUserAndTokenResponse() throws Exception {
//    SignupRequest signupRequest = new SignupRequest();
//    signupRequest.setUsername("mocked-user");
//    signupRequest.setPassword("Toto@123*");
//    signupRequest.setEmail("mocked-user@email.fr");
//
//    mockMvc.perform(post("/api/auth/signup")
//            .contentType("application/json")
//            .content(objectMapper.writeValueAsString(signupRequest)))
//        .andExpect(status().isOk())
//        .andReturn();
//
//    ResultActions loginResult = mockMvc.perform(post("/api/auth/signin")
//              .contentType("application/json")
//              .content(objectMapper.writeValueAsString(signupRequest)))
//          .andExpect(status().isOk())
//          .andExpect(jsonPath("$.data.accessToken").exists())
//          .andExpect(jsonPath("$.data.refreshToken").exists())
//          .andExpect(jsonPath("$.data.id").exists());
//
//    String accessToken = loginResult.andReturn().getResponse().getContentAsString();
//
//    mockMvc.perform(post("/api/auth/refresh-token")
//            .contentType("application/json")
//            .header("Authorization", "Bearer " + accessToken))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.data.accessToken").exists())
//        .andExpect(jsonPath("$.data.refreshToken").exists())
//        .andExpect(jsonPath("$.data.id").exists())
//        .andExpect(jsonPath("$.data.username").exists())
//        .andExpect(jsonPath("$.data.email").exists())
//        .andExpect(jsonPath("$.data.roles").exists());
//  }
}
