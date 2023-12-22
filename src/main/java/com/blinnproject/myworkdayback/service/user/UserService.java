package com.blinnproject.myworkdayback.service.user;

import com.blinnproject.myworkdayback.model.User;
import com.blinnproject.myworkdayback.payload.request.SignupRequest;
import com.blinnproject.myworkdayback.payload.response.UserInfoResponse;

import java.util.Optional;

public interface UserService {

  void throwExceptionIfUsernameIsAlreadyTaken(String username);

  void throwExceptionIfEmailIsAlreadyTaken(String email);

  UserInfoResponse signUp(SignupRequest signUpRequest);

  Optional<User> findByUsername(String username);

  Optional<User> findById(Long userId);
}
