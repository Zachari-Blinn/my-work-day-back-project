package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
  Optional<PasswordResetToken> findByUserEmail(String email);
}
