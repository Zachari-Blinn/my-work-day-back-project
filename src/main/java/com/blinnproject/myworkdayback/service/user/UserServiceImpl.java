package com.blinnproject.myworkdayback.service.user;

import com.blinnproject.myworkdayback.exception.*;
import com.blinnproject.myworkdayback.model.PasswordResetToken;
import com.blinnproject.myworkdayback.model.User;
import com.blinnproject.myworkdayback.payload.request.SignupRequest;
import com.blinnproject.myworkdayback.payload.response.UserInfoResponse;
import com.blinnproject.myworkdayback.repository.PasswordResetTokenRepository;
import com.blinnproject.myworkdayback.repository.UserRepository;
import com.blinnproject.myworkdayback.service.email.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.blinnproject.myworkdayback.util.FormatUtil.generateRandomFourNumbersToken;

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
    if (this.userRepository.existsByUsername(username)) {
      throw new UsernameAlreadyTakenException("Username is already taken.");
    }
  }

  public void throwExceptionIfEmailIsAlreadyTaken(String email) {
    if (this.userRepository.existsByEmail(email)) {
      throw new UsernameAlreadyTakenException("Email is already taken.");
    }
  }

  public UserInfoResponse signUp(SignupRequest signUpRequest) {
    User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getGender(),
      this.encoder.encode(signUpRequest.getPassword()));

    return new UserInfoResponse(this.userRepository.save(user));
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return this.userRepository.findByUsername(username);
  }

  @Override
  public Optional<User> findById(Long userId) {
    return  this.userRepository.findById(userId);
  }

  public void forgotPassword(String email) {
    Optional<User> user = this.userRepository.findByEmail(email);

    // fail silently if user is not found for security reasons
    if (user.isPresent()) {
      // check if token already exists and delete it
      this.passwordResetTokenRepository.findByUserEmail(email).ifPresent(this.passwordResetTokenRepository::delete);

      String token = generateRandomFourNumbersToken();

      createPasswordResetTokenForUser(user.get(), token);

      this.emailService.sendResetPasswordEmail(email, token);
    }
  }

  private void createPasswordResetTokenForUser(User user, String token) {
    PasswordResetToken myToken = new PasswordResetToken(token, user);
    passwordResetTokenRepository.save(myToken);
  }

  public void resetPassword(String email, int token, String newPassword) {
    PasswordResetToken passwordResetToken = this.passwordResetTokenRepository.findByUserEmail(email)
      .orElseThrow(() -> new TokenNotFoundException("Token not found with email: " + email));

    passwordResetToken.setAttempts(passwordResetToken.getAttempts() + 1);

    if (passwordResetToken.getAttempts() > 3) {
      this.passwordResetTokenRepository.delete(passwordResetToken);
      throw new ResetPasswordTokenAttemptsExceededException("Number of attempts exceeded.");
    }

    if (passwordResetToken.getExpiryDate().getTime() < System.currentTimeMillis()) {
      this.passwordResetTokenRepository.delete(passwordResetToken);
      throw new ResetPasswordTokenExpiredException("Token expired.");
    }

    if (passwordResetToken.getToken().equals(String.valueOf(token))) {
      User user = passwordResetToken.getUser();
      user.setPassword(this.encoder.encode(newPassword));
      this.userRepository.save(user);
      this.passwordResetTokenRepository.delete(passwordResetToken);
      this.emailService.sendResetPasswordConfirmationEmail(email);
    } else {
      this.passwordResetTokenRepository.save(passwordResetToken);
      throw new ResetPasswordTokenInvalidException("Token invalid.");
    }
  }
}
