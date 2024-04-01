package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.exception.TokenRefreshException;
import com.blinnproject.myworkdayback.model.entity.RefreshToken;
import com.blinnproject.myworkdayback.model.request.*;
import com.blinnproject.myworkdayback.model.response.GenericResponse;
import com.blinnproject.myworkdayback.model.response.JwtResponse;
import com.blinnproject.myworkdayback.model.response.TokenRefreshResponse;
import com.blinnproject.myworkdayback.model.response.UserInfoResponse;
import com.blinnproject.myworkdayback.security.UserDetailsImpl;
import com.blinnproject.myworkdayback.security.jwt.JwtUtils;
import com.blinnproject.myworkdayback.security.services.RefreshTokenService;
import com.blinnproject.myworkdayback.service.i18n.I18nService;
import com.blinnproject.myworkdayback.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Authentication", description = "Endpoints related to user authentication and authorization.")
@RestController
@RequestMapping(value = "/api/auth/", consumes="application/json", produces="application/json")
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  private final I18nService i18n;
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;
  private final RefreshTokenService refreshTokenService;
  private final UserService userService;

  public AuthController(I18nService i18nService, AuthenticationManager authenticationManager, JwtUtils jwtUtils, RefreshTokenService refreshTokenService, UserService userService) {
    this.i18n = i18nService;
    this.authenticationManager = authenticationManager;
    this.jwtUtils = jwtUtils;
    this.refreshTokenService = refreshTokenService;
    this.userService = userService;
  }

  @Operation(summary = "Authenticate User", description = "Authenticates a user and provides a JWT token for authorization.")
  @PostMapping(value = {"/login", "/signin"})
  public ResponseEntity<GenericResponse<JwtResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    logger.info("Authenticate user: {}", loginRequest);

    Authentication authentication = authenticationManager
      .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    String jwt = jwtUtils.generateJwtToken(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList();

    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
      userDetails.getUsername(), userDetails.getEmail(), roles), i18n.translate("controller.auth.login.successful")));
  }

  @Operation(summary = "Register User", description = "Registers a new user account.")
  @PostMapping(value = {"/register", "/signup"})
  public ResponseEntity<GenericResponse<UserInfoResponse>> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    logger.info("Register user: {}", signUpRequest);

    this.userService.throwExceptionIfUsernameIsAlreadyTaken(signUpRequest.getUsername());
    this.userService.throwExceptionIfEmailIsAlreadyTaken(signUpRequest.getEmail());

    UserInfoResponse createdUser = this.userService.signUp(signUpRequest);

    return ResponseEntity.ok(GenericResponse.success(createdUser, i18n.translate("controller.auth.register.successful")));
  }

  @Operation(summary = "Logout User", description = "Logs out the currently authenticated user.")
  @PostMapping(value = {"logout", "/signout"})
  public ResponseEntity<GenericResponse<Void>> logoutUser() {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    logger.info("Logout user: {}", userDetails);

    this.refreshTokenService.deleteByUserId(userDetails.getId());
    return ResponseEntity.ok(GenericResponse.success(null, i18n.translate("controller.auth.logout.successful")));
  }

  @Operation(summary = "Refresh Token", description = "Renews the access token using a refresh token.")
  @PostMapping("/refreshtoken")
  public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
      .map(refreshTokenService::verifyExpiration)
      .map(RefreshToken::getUser)
      .map(user -> {
        String token = jwtUtils.generateTokenFromUsername(user.getUsername());
        return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
      })
      .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, i18n.translate("refreshToken.error.notFound")));
  }

  @Operation(summary = "Forgot Password", description = "Initiates the process for resetting a forgotten password.")
  @PostMapping("/forgot-password")
  public ResponseEntity<GenericResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest body) throws MessagingException {
    this.userService.forgotPassword(body.getEmail());
    return ResponseEntity.ok(GenericResponse.success(null, i18n.translate("controller.auth.forgot-password.successful")));
  }

  @Operation(summary = "Verify Password Token", description = "Verifies the validity of the reset password token for a given email.")
  @GetMapping("/verify-password-token/{email}/{token}")
  public ResponseEntity<GenericResponse<Void>> validateResetPasswordToken(@PathVariable String email, @PathVariable String token) {
    this.userService.checkResetPasswordToken(email, token);
    return ResponseEntity.ok(GenericResponse.success(null, i18n.translate("controller.auth.reset-password-token.successful")));
  }

  @Operation(summary = "Reset Password", description = "Resets the password for a user account with reset password token.")
  @PostMapping("/reset-password")
  public ResponseEntity<GenericResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest body) {
    this.userService.resetPassword(body.getEmail(), body.getToken(), body.getNewPassword());
    return ResponseEntity.ok(GenericResponse.success(null, i18n.translate("controller.auth.reset-password.successful")));
  }
}