package com.blinnproject.myworkdayback.service.password_reset_token;

import com.blinnproject.myworkdayback.model.entity.PasswordResetToken;
import com.blinnproject.myworkdayback.model.entity.User;
import com.blinnproject.myworkdayback.repository.PasswordResetTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

  private final PasswordResetTokenRepository passwordResetTokenRepository;

  public PasswordResetTokenServiceImpl(PasswordResetTokenRepository passwordResetTokenRepository) {
    this.passwordResetTokenRepository = passwordResetTokenRepository;
  }

  @Override
  public PasswordResetToken create(User user, String token) {
    PasswordResetToken myToken = new PasswordResetToken(token, user);
    return passwordResetTokenRepository.save(myToken);
  }

  @Override
  public Optional<PasswordResetToken> findByUserEmail(String email) {
    return passwordResetTokenRepository.findByUserEmail(email);
  }

  @Override
  public void delete(PasswordResetToken passwordResetToken) {
    passwordResetTokenRepository.delete(passwordResetToken);
  }
}
