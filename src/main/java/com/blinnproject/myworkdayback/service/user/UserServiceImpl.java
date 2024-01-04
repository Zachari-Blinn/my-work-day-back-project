package com.blinnproject.myworkdayback.service.user;

import com.blinnproject.myworkdayback.exception.*;
import com.blinnproject.myworkdayback.model.PasswordResetToken;
import com.blinnproject.myworkdayback.model.User;
import com.blinnproject.myworkdayback.payload.request.SignupRequest;
import com.blinnproject.myworkdayback.payload.response.UserInfoResponse;
import com.blinnproject.myworkdayback.repository.PasswordResetTokenRepository;
import com.blinnproject.myworkdayback.repository.UserRepository;
import com.blinnproject.myworkdayback.service.email.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.blinnproject.myworkdayback.util.FormatUtil.generateSixNumbersRandomlyToken;

@Service
public class UserServiceImpl implements UserService {

  private final PasswordEncoder encoder;

  private final UserRepository userRepository;

  private final PasswordResetTokenRepository passwordResetTokenRepository;

  private final EmailService emailService;

  public UserServiceImpl(PasswordEncoder encoder, UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository, EmailService emailService) {
    this.encoder = encoder;
    this.userRepository = userRepository;
    this.passwordResetTokenRepository = passwordResetTokenRepository;
    this.emailService = emailService;
  }

  public void throwExceptionIfUsernameIsAlreadyTaken(String username) {
    if (Boolean.TRUE.equals(userRepository.existsByUsername(username))) {
      throw new UsernameAlreadyTakenException("Username is already taken.");
    }
  }

  public void throwExceptionIfEmailIsAlreadyTaken(String email) {
    if (Boolean.TRUE.equals(userRepository.existsByEmail(email))) {
      throw new UsernameAlreadyTakenException("Email is already taken.");
    }
  }

  @Transactional
  public UserInfoResponse signUp(SignupRequest signUpRequest) {
    User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getGender(),
      this.encoder.encode(signUpRequest.getPassword()));

    return new UserInfoResponse(userRepository.save(user));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findById(Long userId) {
    return userRepository.findById(userId);
  }

  @Transactional
  public void forgotPassword(String email) throws MessagingException {
    Optional<User> user = userRepository.findByEmail(email);

    // fail silently if user is not found for security reasons
    if (user.isPresent()) {
      // check if token already exists and delete it
      passwordResetTokenRepository.findByUserEmail(email).ifPresent(passwordResetTokenRepository::delete);

      String token = generateSixNumbersRandomlyToken();

      this.createPasswordResetTokenForUser(user.get(), encoder.encode(token));

      this.emailService.sendForgotPasswordEmail(email, token, user.get());
    }
  }

  private void createPasswordResetTokenForUser(User user, String token) {
    PasswordResetToken myToken = new PasswordResetToken(token, user);
    passwordResetTokenRepository.save(myToken);
  }

  @Transactional
  public void resetPassword(String email, int token, String newPassword) {
    PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUserEmail(email)
      .orElseThrow(() -> new TokenNotFoundException("Token not found with email: " + email));

    passwordResetToken.setAttempts(passwordResetToken.getAttempts() + 1);

    if (passwordResetToken.getAttempts() > 3) {
      passwordResetTokenRepository.delete(passwordResetToken);
      throw new ResetPasswordTokenAttemptsExceededException("Number of attempts exceeded.");
    }

    if (passwordResetToken.getExpiryDate().getTime() < System.currentTimeMillis()) {
      passwordResetTokenRepository.delete(passwordResetToken);
      throw new ResetPasswordTokenExpiredException("Token expired.");
    }

    if (encoder.matches(String.valueOf(token), passwordResetToken.getToken())) {
      User user = passwordResetToken.getUser();
      user.setPassword(encoder.encode(newPassword));
      userRepository.save(user);
      passwordResetTokenRepository.delete(passwordResetToken);
      emailService.sendResetPasswordSuccessEmail(email);
    } else {
      passwordResetTokenRepository.save(passwordResetToken);
      throw new ResetPasswordTokenInvalidException("Token invalid.");
    }
  }
}
