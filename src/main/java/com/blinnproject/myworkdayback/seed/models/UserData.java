package com.blinnproject.myworkdayback.seed.models;

import com.blinnproject.myworkdayback.model.User;
import com.blinnproject.myworkdayback.repository.UserRepository;
import com.blinnproject.myworkdayback.seed.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserData {

  private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder encoder;

  public List<User> userList = new ArrayList<User>();

  public void load() {
    List<User> result = null;
    if (userRepository.count() == 0) {
      logger.info("Seeding users...");

      this.userList.add(new User("jean-sebastien", "jean-sebastien@fake-email.fr", encoder.encode("Toto@123*")));
      this.userList.add(new User("jean-baptiste", "jean-baptiste@fake-email.fr", encoder.encode("Toto@123*")));
      // Add more user here

      userRepository.saveAll(this.userList);
      logger.info(String.valueOf(userRepository.count()) + " user successfully loaded!");
    }
  }

  public User findUserByUsername(String username) throws ChangeSetPersister.NotFoundException {
    return this.userList.stream().filter(user -> user.getUsername().equals("jean-sebastien")).findFirst().orElseThrow(ChangeSetPersister.NotFoundException::new);
  }
}
