package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.model.request.UpdateUserPasswordDTO;
import com.blinnproject.myworkdayback.model.request.UpdateUserProfileDTO;
import com.blinnproject.myworkdayback.model.response.GenericResponse;
import com.blinnproject.myworkdayback.security.UserDetailsImpl;
import com.blinnproject.myworkdayback.service.i18n.I18nService;
import com.blinnproject.myworkdayback.service.profile_picture.ProfilePictureService;
import com.blinnproject.myworkdayback.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Tag(name="User", description = "Endpoints related to user operations")
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/api/user/")
public class UserController {

  private final I18nService i18n;
  private final ProfilePictureService profilePictureService;
  private final UserService userService;

  public UserController(I18nService i18nService, ProfilePictureService profilePictureService, UserService userService) {
    this.i18n = i18nService;
    this.userService = userService;
    this.profilePictureService = profilePictureService;
  }

  @Operation(summary = "Upload Profile Picture", description = "Uploads a profile picture for the authenticated user.")
  @PostMapping(value = "profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/json")
  public ResponseEntity<GenericResponse<String>> uploadProfilePicture(
      @RequestParam("picture") MultipartFile file,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) throws IOException {
    String fileName = this.profilePictureService.uploadProfilePicture(file, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(fileName, i18n.translate("controller.user.upload-profile-picture.successful")));
  }

  @Operation(summary = "Get Profile Picture by Username", description = "Retrieves the profile picture of a user by username.")
  @GetMapping(value = "{username}/profile-picture", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<?> getProfilePictureByUsername(@PathVariable("username") String username) {
    byte[] image = this.profilePictureService.getProfilePictureByUsername(username);

    return ResponseEntity.status(HttpStatus.OK)
      .contentType(MediaType.valueOf("image/png"))
      .body(image);
  }

  @Operation(summary = "Update user profile", description = "Updates the profile of the authenticated user.")
  @PatchMapping(value = "profile", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GenericResponse<UpdateUserProfileDTO>> updateProfile(
      @RequestBody UpdateUserProfileDTO updateUserProfileDTO,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    UpdateUserProfileDTO updatedUserProfile = userService.updateProfile(updateUserProfileDTO, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(updatedUserProfile, i18n.translate("controller.user.update-profile.successful")));
  }

  @Operation(summary = "Change user password", description = "Changes the password of the authenticated user.")
  @PatchMapping(value = "password", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GenericResponse<Void>> changePassword(
      @RequestBody UpdateUserPasswordDTO updateUserPasswordDTO,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    userService.changePassword(updateUserPasswordDTO, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(null, i18n.translate("controller.user.change-password.successful")));
  }
}
