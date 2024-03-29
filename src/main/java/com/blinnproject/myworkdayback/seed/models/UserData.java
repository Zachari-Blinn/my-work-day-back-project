package com.blinnproject.myworkdayback.seed.models;

import com.blinnproject.myworkdayback.model.enums.EGender;
import com.blinnproject.myworkdayback.model.entity.User;
import com.blinnproject.myworkdayback.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserData {

  private static final Logger logger = LoggerFactory.getLogger(UserData.class);

  private final UserRepository userRepository;

  private final PasswordEncoder encoder;

  public UserData(UserRepository userRepository, PasswordEncoder encoder) {
    this.userRepository = userRepository;
    this.encoder = encoder;
  }

  public List<User> userList = new ArrayList<>();

  public void load() {
    if (userRepository.count() == 0) {
      logger.info("Seeding users...");

      this.userList.add(new User("jean-sebastien", "jean-sebastien@fake-email.fr", EGender.MAN, encoder.encode("Toto@123*")));
      this.userList.add(new User("jean-baptiste", "jean-baptiste@fake-email.fr", EGender.NOT_SPECIFIED, encoder.encode("Toto@123*")));
      this.userList.add(new User("test", "test@test.fr", EGender.NOT_SPECIFIED, encoder.encode("test")));
      // Add more user here

      userRepository.saveAll(this.userList);
      logger.info("{} user successfully loaded!", userRepository.count());
    }
  }

  public User findUserByUsername(String username) throws ChangeSetPersister.NotFoundException {
    return this.userList.stream().filter(user -> user.getUsername().equals(username)).findFirst().orElseThrow(ChangeSetPersister.NotFoundException::new);
  }
}
