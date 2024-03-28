package com.blinnproject.myworkdayback.service.profile_picture;

import com.blinnproject.myworkdayback.exception.ProfilePictureIsEmptyException;
import com.blinnproject.myworkdayback.exception.ProfilePictureNotFoundException;
import com.blinnproject.myworkdayback.model.entity.ProfilePicture;
import com.blinnproject.myworkdayback.model.entity.User;
import com.blinnproject.myworkdayback.repository.ProfilePictureRepository;
import com.blinnproject.myworkdayback.service.user.UserService;
import com.blinnproject.myworkdayback.util.ImageUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class ProfilePictureServiceImpl implements ProfilePictureService {

  private final ProfilePictureRepository profilePictureRepository;
  private final UserService userService;

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
    ProfilePicture profilePicture = profilePictureRepository.findByName(name).orElseThrow(() -> new ProfilePictureNotFoundException("Profile picture not found"));

    return ProfilePicture.builder()
      .name(profilePicture.getName())
      .type(profilePicture.getType())
      .fileData(ImageUtil.decompressImage(profilePicture.getFileData())).build();
  }

  @Transactional
  public byte[] getProfilePictureByUsername(String username) {
    ProfilePicture profilePicture = profilePictureRepository.findByUserUsername(username).orElseThrow(ProfilePictureIsEmptyException::new);

    return ImageUtil.decompressImage(profilePicture.getFileData());
  }


}
