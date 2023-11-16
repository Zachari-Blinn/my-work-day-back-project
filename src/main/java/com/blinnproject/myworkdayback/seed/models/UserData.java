package com.blinnproject.myworkdayback.seed.models;

import com.blinnproject.myworkdayback.model.User;
import com.blinnproject.myworkdayback.repository.UserRepository;
import com.blinnproject.myworkdayback.seed.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserData {

  private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder encoder;

  public void loadUserData() {
    if (userRepository.count() == 0) {
      logger.info("Seeding users...");

      User user1 = new User("jean-sebastien", "jean-sebastien@fake-email.fr", encoder.encode("Toto@123*"));
      User user2 = new User("jean-baptiste", "jean-baptiste@fake-email.fr", encoder.encode("Toto@123*"));

      userRepository.save(user1);
      userRepository.save(user2);
    }
    logger.info(String.valueOf(userRepository.count()) + " successfully loaded!");
  }
}
