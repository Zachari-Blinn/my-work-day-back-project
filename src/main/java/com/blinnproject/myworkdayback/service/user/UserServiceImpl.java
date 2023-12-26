package com.blinnproject.myworkdayback.service.user;

import com.blinnproject.myworkdayback.exception.UsernameAlreadyTakenException;
import com.blinnproject.myworkdayback.model.User;
import com.blinnproject.myworkdayback.payload.request.SignupRequest;
import com.blinnproject.myworkdayback.payload.response.UserInfoResponse;
import com.blinnproject.myworkdayback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

  private final PasswordEncoder encoder;

  private final UserRepository userRepository;

  public UserServiceImpl(PasswordEncoder encoder, UserRepository userRepository) {
    this.encoder = encoder;
    this.userRepository = userRepository;
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
}
