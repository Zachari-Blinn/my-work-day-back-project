package com.blinnproject.myworkdayback.service.user;

import com.blinnproject.myworkdayback.payload.request.SignupRequest;
import com.blinnproject.myworkdayback.payload.response.UserInfoResponse;

public interface UserService {

  void throwExceptionIfUsernameIsAlreadyTaken(String username);

  void throwExceptionIfEmailIsAlreadyTaken(String email);

  UserInfoResponse signUp(SignupRequest signUpRequest);
}
