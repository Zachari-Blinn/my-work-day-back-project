package com.blinnproject.myworkdayback.controller;

import java.util.List;
import java.util.stream.Collectors;
import com.blinnproject.myworkdayback.exception.TokenRefreshException;
import com.blinnproject.myworkdayback.model.RefreshToken;
import com.blinnproject.myworkdayback.model.User;
import com.blinnproject.myworkdayback.payload.request.LoginRequest;
import com.blinnproject.myworkdayback.payload.request.SignupRequest;
import com.blinnproject.myworkdayback.payload.request.TokenRefreshRequest;
import com.blinnproject.myworkdayback.payload.response.JwtResponse;
import com.blinnproject.myworkdayback.payload.response.MessageResponse;
import com.blinnproject.myworkdayback.payload.response.TokenRefreshResponse;
import com.blinnproject.myworkdayback.repository.UserRepository;
import com.blinnproject.myworkdayback.security.jwt.JwtUtils;
import com.blinnproject.myworkdayback.security.services.RefreshTokenService;
import com.blinnproject.myworkdayback.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  RefreshTokenService refreshTokenService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
      .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    String jwt = jwtUtils.generateJwtToken(userDetails);

    List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
      .collect(Collectors.toList());

    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

    User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);

    return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
      userDetails.getUsername(), userDetails.getEmail(), roles, user));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
      encoder.encode(signUpRequest.getPassword()));

    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = userDetails.getId();
    refreshTokenService.deleteByUserId(userId);
    return ResponseEntity.ok(new MessageResponse("Log out successful!"));
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
      .map(refreshTokenService::verifyExpiration)
      .map(RefreshToken::getUser)
      .map(user -> {
        String token = jwtUtils.generateTokenFromUsername(user.getUsername());
        return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
      })
      .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
        "Refresh token is not in database!"));
  }
}