package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.model.ProfilePicture;
import com.blinnproject.myworkdayback.payload.response.GenericResponse;
import com.blinnproject.myworkdayback.security.UserDetailsImpl;
import com.blinnproject.myworkdayback.service.profile_picture.ProfilePictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/user/")
public class UserController {

  private final ProfilePictureService profilePictureService;

  public UserController(ProfilePictureService profilePictureService) {
    this.profilePictureService = profilePictureService;
  }

  @PostMapping("profile-picture")
  public ResponseEntity<GenericResponse<String>> uploadProfilePicture(@RequestParam("picture") MultipartFile file) throws IOException {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    String fileName = this.profilePictureService.uploadProfilePicture(file, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(fileName, "Profile picture was uploaded successfully!"));
  }

  @GetMapping("{username}/profile-picture")
  public ResponseEntity<?> getProfilePictureByUsername(@PathVariable("username") String username) {
    byte[] image = this.profilePictureService.getProfilePictureByUsername(username);

    return ResponseEntity.status(HttpStatus.OK)
      .contentType(MediaType.valueOf("image/png"))
      .body(image);
  }
}
