package com.blinnproject.myworkdayback.service.user;

import com.blinnproject.myworkdayback.model.entity.User;
import com.blinnproject.myworkdayback.model.request.SignupRequest;
import com.blinnproject.myworkdayback.model.request.UpdateUserPasswordDTO;
import com.blinnproject.myworkdayback.model.request.UpdateUserProfileDTO;
import com.blinnproject.myworkdayback.model.response.UserInfoResponse;
import jakarta.mail.MessagingException;

import java.util.Optional;

public interface UserService {

  void throwExceptionIfUsernameIsAlreadyTaken(String username);

  void throwExceptionIfEmailIsAlreadyTaken(String email);

  UserInfoResponse signUp(SignupRequest signUpRequest);

  Optional<User> findById(Long userId);

  void forgotPassword(String email) throws MessagingException;

  void resetPassword(String email, int token, String newPassword);

  void checkResetPasswordToken(String email, String token);

  UpdateUserProfileDTO updateProfile(UpdateUserProfileDTO updateUserProfileDTO, Long userId);

  void changePassword(UpdateUserPasswordDTO updateUserPasswordDTO, Long userId);
}
