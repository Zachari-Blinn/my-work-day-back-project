package com.blinnproject.myworkdayback.security.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import com.blinnproject.myworkdayback.exception.TokenRefreshException;
import com.blinnproject.myworkdayback.model.entity.RefreshToken;
import com.blinnproject.myworkdayback.repository.RefreshTokenRepository;
import com.blinnproject.myworkdayback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {
  @Value("${raise.app.jwtRefreshExpirationMs}")
  private Long refreshTokenDurationMs;

  private final RefreshTokenRepository refreshTokenRepository;

  private final UserRepository userRepository;

  public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  @Transactional
  public RefreshToken createRefreshToken(Long userId) {
    Optional<RefreshToken> existingToken = refreshTokenRepository.findByUserId(userId);

    RefreshToken refreshToken;

    if (existingToken.isPresent()) {
      refreshToken = existingToken.get();

    } else {
      refreshToken = new RefreshToken();
      refreshToken.setUser(userRepository.findById(userId).orElse(null));
    }

    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
    refreshToken.setToken(UUID.randomUUID().toString());

    return refreshTokenRepository.save(refreshToken);
  }

  @Transactional
  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
    }

    return token;
  }

  @Transactional
  public void deleteByUserId(Long userId) {
    refreshTokenRepository.deleteByUser(userRepository.findById(userId).orElse(null));
  }
}