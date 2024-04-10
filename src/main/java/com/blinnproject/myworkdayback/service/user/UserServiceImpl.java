package com.blinnproject.myworkdayback.service.user;

import com.blinnproject.myworkdayback.exception.*;
import com.blinnproject.myworkdayback.model.entity.PasswordResetToken;
import com.blinnproject.myworkdayback.model.entity.User;
import com.blinnproject.myworkdayback.model.request.SignupRequest;
import com.blinnproject.myworkdayback.model.request.UpdateUserPasswordDTO;
import com.blinnproject.myworkdayback.model.request.UpdateUserProfileDTO;
import com.blinnproject.myworkdayback.model.response.UserInfoResponse;
import com.blinnproject.myworkdayback.repository.UserRepository;
import com.blinnproject.myworkdayback.service.email.EmailService;
import com.blinnproject.myworkdayback.service.password_reset_token.PasswordResetTokenService;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.blinnproject.myworkdayback.util.FormatUtil.generateSixNumbersRandomlyToken;

@Service
public class UserServiceImpl implements UserService {

  private final ModelMapper modelMapper;

  private final PasswordEncoder encoder;

  private final UserRepository userRepository;

  private final PasswordResetTokenService passwordResetTokenService;

  private final EmailService emailService;

  public UserServiceImpl(ModelMapper modelMapper, PasswordEncoder encoder, UserRepository userRepository, EmailService emailService, PasswordResetTokenService passwordResetTokenService) {
    this.modelMapper = modelMapper;
    this.encoder = encoder;
    this.userRepository = userRepository;
    this.emailService = emailService;
    this.passwordResetTokenService = passwordResetTokenService;
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

  public void forgotPassword(String email) throws MessagingException {
    Optional<User> user = userRepository.findByEmail(email);

    // fail silently if user is not found for security reasons
    if (user.isPresent()) {
      // check if token already exists and delete it
      passwordResetTokenService.findByUserEmail(email).ifPresent(passwordResetTokenService::delete);

      String token = generateSixNumbersRandomlyToken();

      passwordResetTokenService.create(user.get(), encoder.encode(token));

      this.emailService.sendForgotPasswordEmail(email, token, user.get());
    }
  }

  @Transactional
  public void resetPassword(String email, int token, String newPassword) {
    PasswordResetToken passwordResetToken = passwordResetTokenService.findByUserEmail(email)
      .orElseThrow(() -> new TokenNotFoundException("Token not found with email: " + email));

    passwordResetToken.setAttempts(passwordResetToken.getAttempts() + 1);

    if (passwordResetToken.getAttempts() > 3) {
      passwordResetTokenService.delete(passwordResetToken);
      throw new ResetPasswordTokenAttemptsExceededException("Number of attempts exceeded.");
    }

    if (passwordResetToken.getExpiryDate().getTime() < System.currentTimeMillis()) {
      passwordResetTokenService.delete(passwordResetToken);
      throw new ResetPasswordTokenExpiredException("Token expired.");
    }

    if (encoder.matches(String.valueOf(token), passwordResetToken.getToken())) {
      User user = passwordResetToken.getUser();
      user.setPassword(encoder.encode(newPassword));
      userRepository.save(user);
      passwordResetTokenService.delete(passwordResetToken);
      emailService.sendResetPasswordSuccessEmail(email);
    } else {
      passwordResetTokenService.create(passwordResetToken.getUser(), passwordResetToken.getToken());
      throw new ResetPasswordTokenInvalidException("Token invalid.");
    }
  }

  public void checkResetPasswordToken(String email, String token) {
    PasswordResetToken passwordResetToken = passwordResetTokenService.findByUserEmail(email)
      .orElseThrow(() -> new TokenNotFoundException("Token not found with email: " + email));

    if (passwordResetToken.getAttempts() > 3) {
      passwordResetTokenService.delete(passwordResetToken);
      throw new ResetPasswordTokenAttemptsExceededException("Number of attempts exceeded.");
    }

    if (passwordResetToken.getExpiryDate().getTime() < System.currentTimeMillis()) {
      passwordResetTokenService.delete(passwordResetToken);
      throw new ResetPasswordTokenExpiredException("Token expired.");
    }

    if (!encoder.matches(String.valueOf(token), passwordResetToken.getToken())) {
      throw new ResetPasswordTokenInvalidException("Token invalid.");
    }
  }

  @Override
  public UpdateUserProfileDTO updateProfile(UpdateUserProfileDTO updateUserProfileDTO, Long userId) {
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

    if (updateUserProfileDTO.getUsername() != null) {
      user.setUsername(updateUserProfileDTO.getUsername());
    }
    if (updateUserProfileDTO.getEmail() != null) {
      user.setEmail(updateUserProfileDTO.getEmail());
    }
    if (updateUserProfileDTO.getGender() != null) {
      user.setGender(updateUserProfileDTO.getGender());
    }

    return modelMapper.map(userRepository.save(user), UpdateUserProfileDTO.class);
  }

  @Override
  public void changePassword(UpdateUserPasswordDTO updateUserPasswordDTO, Long userId) {
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

    if (!encoder.matches(updateUserPasswordDTO.getOldPassword(), user.getPassword())) {
      throw new InvalidPasswordException("Old password is incorrect.");
    }

    user.setPassword(encoder.encode(updateUserPasswordDTO.getNewPassword()));
    userRepository.save(user);
  }
}
