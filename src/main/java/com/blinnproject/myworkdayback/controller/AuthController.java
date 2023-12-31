package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.exception.TokenRefreshException;
import com.blinnproject.myworkdayback.model.RefreshToken;
import com.blinnproject.myworkdayback.payload.request.*;
import com.blinnproject.myworkdayback.payload.response.GenericResponse;
import com.blinnproject.myworkdayback.payload.response.JwtResponse;
import com.blinnproject.myworkdayback.payload.response.TokenRefreshResponse;
import com.blinnproject.myworkdayback.payload.response.UserInfoResponse;
import com.blinnproject.myworkdayback.security.UserDetailsImpl;
import com.blinnproject.myworkdayback.security.jwt.JwtUtils;
import com.blinnproject.myworkdayback.security.services.RefreshTokenService;
import com.blinnproject.myworkdayback.service.i18n.I18nService;
import com.blinnproject.myworkdayback.service.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

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

  @PostMapping(value = {"/login", "/signin"})
  public ResponseEntity<GenericResponse<JwtResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
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

  @PostMapping(value = {"/register", "/signup"})
  public ResponseEntity<GenericResponse<UserInfoResponse>> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    this.userService.throwExceptionIfUsernameIsAlreadyTaken(signUpRequest.getUsername());
    this.userService.throwExceptionIfEmailIsAlreadyTaken(signUpRequest.getEmail());

    UserInfoResponse createdUser = this.userService.signUp(signUpRequest);

    return ResponseEntity.ok(GenericResponse.success(createdUser, i18n.translate("controller.auth.register.successful")));
  }

  @PostMapping(value = {"logout", "/signout"})
  public ResponseEntity<GenericResponse<?>> logoutUser() {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    this.refreshTokenService.deleteByUserId(userDetails.getId());
    return ResponseEntity.ok(GenericResponse.success(null, i18n.translate("controller.auth.logout.successful")));
  }

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

  @PostMapping("/forgot-password")
  public ResponseEntity<GenericResponse<?>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest body) throws MessagingException {
    this.userService.forgotPassword(body.getEmail());
    return ResponseEntity.ok(GenericResponse.success(null, i18n.translate("controller.auth.forgot-password.successful")));
  }

  @PostMapping("/reset-password")
  public ResponseEntity<GenericResponse<?>> resetPassword(@Valid @RequestBody ResetPasswordRequest body) {
    this.userService.resetPassword(body.getEmail(), body.getToken(), body.getNewPassword());
    return ResponseEntity.ok(GenericResponse.success(null, i18n.translate("controller.auth.reset-password.successful")));
  }
}