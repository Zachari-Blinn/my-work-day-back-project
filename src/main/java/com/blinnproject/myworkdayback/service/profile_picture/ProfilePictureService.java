package com.blinnproject.myworkdayback.service.profile_picture;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface ProfilePictureService {

  String uploadProfilePicture(MultipartFile file, Long userId) throws IOException;

  byte[] getProfilePictureByUsername(String username);
}
