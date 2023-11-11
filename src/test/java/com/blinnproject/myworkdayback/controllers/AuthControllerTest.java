package com.blinnproject.myworkdayback.controllers;

import com.blinnproject.myworkdayback.controller.AuthController;
import com.blinnproject.myworkdayback.model.User;
import com.blinnproject.myworkdayback.payload.request.LoginRequest;
import com.blinnproject.myworkdayback.payload.request.SignupRequest;
import com.blinnproject.myworkdayback.repository.UserRepository;
import com.blinnproject.myworkdayback.security.jwt.JwtUtils;
import com.blinnproject.myworkdayback.security.services.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthenticationManager authenticationManager;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private PasswordEncoder encoder;

  @MockBean
  private JwtUtils jwtUtils;

  @MockBean
  private RefreshTokenService refreshTokenService;

  @Autowired
  private ObjectMapper objectMapper;

  private User user;
  private SignupRequest signupRequest;
  private LoginRequest loginRequest;

  @BeforeEach
  public void init() {
    user = User.builder().email("jean-jacques@fake-email.fr").username("jean-jacques").password("Toto@1234*").build();
    userRepository.save(user);
    signupRequest = SignupRequest.builder().email("jean-jacques@fake-email.fr").username("jean-jacques").password("Toto@1234*").build();
    loginRequest = LoginRequest.builder().username("jean-jacques").password("Toto@1234*").build();
  }

  @Test
  public void AuthController_RegisterUser_ReturnCreated() throws Exception {
    ResultActions response = mockMvc.perform(post("/api/auth/signup")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(signupRequest)));

    response.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
  }

//  @Test
//  public void AuthController_AuthenticateUser_ReturnCreated() throws Exception {
//    BDDMockito.given(authenticationManager.authenticate(ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArguments());
//
//    ResultActions response = mockMvc.perform(post("/api/auth/signin")
//      .contentType(MediaType.APPLICATION_JSON)
//      .content(objectMapper.writeValueAsString(loginRequest)));
//
//    response.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
//  }
}
