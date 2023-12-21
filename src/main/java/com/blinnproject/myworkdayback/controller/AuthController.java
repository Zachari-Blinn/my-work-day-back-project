package com.blinnproject.myworkdayback.controller;

import java.util.List;
import java.util.stream.Collectors;
import com.blinnproject.myworkdayback.exception.TokenRefreshException;
import com.blinnproject.myworkdayback.model.RefreshToken;
import com.blinnproject.myworkdayback.payload.request.LoginRequest;
import com.blinnproject.myworkdayback.payload.request.SignupRequest;
import com.blinnproject.myworkdayback.payload.request.TokenRefreshRequest;
import com.blinnproject.myworkdayback.payload.response.*;
import com.blinnproject.myworkdayback.security.jwt.JwtUtils;
import com.blinnproject.myworkdayback.security.services.RefreshTokenService;
import com.blinnproject.myworkdayback.security.UserDetailsImpl;
import com.blinnproject.myworkdayback.service.user.UserService;
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
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  RefreshTokenService refreshTokenService;

  @Autowired
  UserService userService;

  @PostMapping("/signin")
  public ResponseEntity<GenericResponse<?>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager
      .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    String jwt = jwtUtils.generateJwtToken(userDetails);

    List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
      .collect(Collectors.toList());

    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
      userDetails.getUsername(), userDetails.getEmail(), roles), "User logged in successfully!"));
  }

  @PostMapping("/signup")
  public ResponseEntity<GenericResponse<UserInfoResponse>> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    this.userService.throwExceptionIfUsernameIsAlreadyTaken(signUpRequest.getUsername());
    this.userService.throwExceptionIfEmailIsAlreadyTaken(signUpRequest.getEmail());

    UserInfoResponse createdUser = this.userService.signUp(signUpRequest);

    return ResponseEntity.ok(GenericResponse.success(createdUser, "User registered successfully!"));
  }

  @PostMapping("/signout")
  public ResponseEntity<GenericResponse<?>> logoutUser() {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    this.refreshTokenService.deleteByUserId(userDetails.getId());
    return ResponseEntity.ok(GenericResponse.success(null, "Log out successful!"));
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