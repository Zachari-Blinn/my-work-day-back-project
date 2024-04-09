package com.blinnproject.myworkdayback.controllers;

import com.blinnproject.myworkdayback.AbstractIntegrationTest;
import com.blinnproject.myworkdayback.model.request.LoginRequest;
import com.blinnproject.myworkdayback.model.request.SignupRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest extends AbstractIntegrationTest {

  private static final String API_PATH = "/api/auth";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @Order(value = 1)
  @DisplayName("Register a new user")
  void AuthController_Register_ReturnSavedUser() throws Exception {
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setUsername("IcIaleaL");
    signupRequest.setPassword("Toto@123*");
    signupRequest.setEmail("mocked-user@email.fr");

    mockMvc.perform(post(API_PATH + "/signup")
      .contentType("application/json")
      .content(objectMapper.writeValueAsString(signupRequest)))
      .andExpect(status().isOk());
  }

  @Test
  @Order(value = 2)
  @DisplayName("Register a new user with blank values")
  void AuthController_RegisterWithBlankValues_ReturnError() throws Exception {
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setUsername("");
    signupRequest.setPassword("");
    signupRequest.setEmail("");

    ResultActions result = mockMvc.perform(post(API_PATH + "/signup")
      .contentType("application/json")
      .content(objectMapper.writeValueAsString(signupRequest)))
      .andExpect(status().isBadRequest());

    String responseContent = result.andReturn().getResponse().getContentAsString();

    assertTrue(responseContent.contains("\"password\""));
    assertTrue(responseContent.contains("\"email\""));
    assertTrue(responseContent.contains("\"username\""));
  }

  @Test
  @Order(value = 3)
  @DisplayName("Login with a registered user")
  void AuthController_Login_ReturnUserAndTokenResponse() throws Exception {
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setUsername("AvYinDna");
    signupRequest.setPassword("Toto@123*");
    signupRequest.setEmail("atopgeezaboardsignal@email.fr");

    mockMvc.perform(post(API_PATH + "/signup")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(signupRequest)))
        .andExpect(status().isOk());

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUsername("AvYinDna");
    loginRequest.setPassword("Toto@123*");

    mockMvc.perform(post(API_PATH + "/signin")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.accessToken").exists())
        .andExpect(jsonPath("$.data.refreshToken").exists())
        .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
        .andExpect(jsonPath("$.data.expirationDate").exists())
        .andExpect(jsonPath("$.data.id").exists())
        .andExpect(jsonPath("$.data.username").exists())
        .andExpect(jsonPath("$.data.email").exists())
        .andExpect(jsonPath("$.data.roles").exists());
  }

  @Test
  @Order(value = 4)
  @DisplayName("Login with wrong credentials")
  void AuthController_LoginWithWrongCredentials_ReturnUnauthorizedError() throws Exception {
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setUsername("PSimheIn");
    signupRequest.setPassword("Toto@123*");
    signupRequest.setEmail("atopalongsiderevenue@email.fr");

    mockMvc.perform(post(API_PATH + "/signup")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(signupRequest)))
        .andExpect(status().isOk());

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUsername("PSimheIn");
    loginRequest.setPassword("Wrong-password@123*");

    mockMvc.perform(post(API_PATH + "/signin")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Order(value = 5)
  @DisplayName("Login with blank values")
  void AuthController_LoginWithBlankValues_ReturnError() throws Exception {
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUsername("");
    loginRequest.setPassword("");

    ResultActions result = mockMvc.perform(post(API_PATH + "/signin")
      .contentType("application/json")
      .content(objectMapper.writeValueAsString(loginRequest)))
      .andExpect(status().isBadRequest());

    String responseContent = result.andReturn().getResponse().getContentAsString();

    assertTrue(responseContent.contains("\"password\""));
    assertTrue(responseContent.contains("\"username\""));
  }

  @Test
  @DisplayName("Get refresh token after login")
  void AuthController_RefreshToken_ReturnUserAndTokenResponse() throws Exception {
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setUsername("RseNianS");
    signupRequest.setEmail("rsenians@fake-email.com");
    signupRequest.setPassword("Toto@123*");

    mockMvc.perform(post(API_PATH + "/signup")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(signupRequest)))
        .andExpect(status().isOk());

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUsername("RseNianS");
    loginRequest.setPassword("Toto@123*");

    ResultActions result = mockMvc.perform(post(API_PATH + "/signin")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk());

    String responseContent = result.andReturn().getResponse().getContentAsString();

    String refreshToken = objectMapper.readTree(responseContent).get("data").get("refreshToken").asText();

    mockMvc.perform(post(API_PATH + "/refreshtoken")
            .contentType("application/json")
            .content("{\"refreshToken\": \"" + refreshToken + "\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").exists())
        .andExpect(jsonPath("$.accessToken").isNotEmpty())
        .andExpect(jsonPath("$.refreshToken").exists())
        .andExpect(jsonPath("$.refreshToken").isNotEmpty())
        .andExpect(jsonPath("$.tokenType").exists())
        .andExpect(jsonPath("$.tokenType").value("Bearer"))
        .andExpect(jsonPath("$.expirationDate").exists())
        .andExpect(jsonPath("$.expirationDate").isNotEmpty());
  }

  @Test
  @DisplayName("Get refresh token with invalid token")
  void AuthController_RefreshTokenWithInvalidToken_ReturnUnauthorizedError() throws Exception {
    mockMvc.perform(post(API_PATH + "/refreshtoken")
            .contentType("application/json")
            .content("{\"refreshToken\": \"invalid-token\"}"))
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("Get refresh token with blank token")
  void AuthController_RefreshTokenWithBlankToken_ReturnUnauthorizedError() throws Exception {
    mockMvc.perform(post(API_PATH + "/refreshtoken")
            .contentType("application/json")
            .content("{\"refreshToken\": \"\"}"))
        .andExpect(status().isBadRequest());
  }
}
