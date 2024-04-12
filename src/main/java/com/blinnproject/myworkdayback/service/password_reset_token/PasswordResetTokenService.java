package com.blinnproject.myworkdayback.service.password_reset_token;

import com.blinnproject.myworkdayback.model.entity.PasswordResetToken;
import com.blinnproject.myworkdayback.model.entity.User;

import java.util.Optional;

public interface PasswordResetTokenService {
  PasswordResetToken create(User user, String token);

  Optional<PasswordResetToken> findByUserEmail(String email);

  void delete(PasswordResetToken passwordResetToken);
}
