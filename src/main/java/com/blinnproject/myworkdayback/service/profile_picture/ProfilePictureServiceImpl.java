package com.blinnproject.myworkdayback.service.profile_picture;

import com.blinnproject.myworkdayback.exception.ProfilePictureIsEmptyException;
import com.blinnproject.myworkdayback.model.ProfilePicture;
import com.blinnproject.myworkdayback.model.User;
import com.blinnproject.myworkdayback.repository.ProfilePictureRepository;
import com.blinnproject.myworkdayback.service.user.UserService;
import com.blinnproject.myworkdayback.util.ImageUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ProfilePictureServiceImpl implements ProfilePictureService {

  private final ProfilePictureRepository profilePictureRepository;
  private UserService userService;

  public ProfilePictureServiceImpl(ProfilePictureRepository profilePictureRepository, UserService userService) {
    this.profilePictureRepository = profilePictureRepository;
    this.userService = userService;
  }

  public String uploadProfilePicture(MultipartFile file, Long userId) throws IOException {
    User user = this.userService.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + userId));
    this.profilePictureRepository.save(
      ProfilePicture.builder()
        .name(file.getOriginalFilename())
        .user(user)
        .type(file.getContentType())
        .fileData(ImageUtil.compressImage(file.getBytes())).build()
    );

    return file.getOriginalFilename();
  }

  @Transactional
  public ProfilePicture getInfoByUsername(String name) {
    Optional<ProfilePicture> profilePicture = profilePictureRepository.findByName(name);

    return ProfilePicture.builder()
      .name(profilePicture.get().getName())
      .type(profilePicture.get().getType())
      .fileData(ImageUtil.decompressImage(profilePicture.get().getFileData())).build();
  }

  @Transactional
  public byte[] getProfilePictureByUsername(String username) {
    Optional<ProfilePicture> profilePicture = profilePictureRepository.findByUserUsername(username);
    if (profilePicture.isEmpty()) {
      throw new ProfilePictureIsEmptyException();
    }
    return ImageUtil.decompressImage(profilePicture.get().getFileData());
  }


}
