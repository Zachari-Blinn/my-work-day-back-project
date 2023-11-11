package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.Gender;
import com.blinnproject.myworkdayback.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  public void UserRepository_SaveAll_ReturnSavedUser() {
    User user = User.builder()
      .username("jean-sebastien")
      .email("jean-sebastien@fake-email.fr")
      .gender(Gender.MAN)
      .password("Toto@2021*").build();

    User savedUser = userRepository.save(user);

    Assertions.assertThat(savedUser).isNotNull();
    Assertions.assertThat(savedUser.getId()).isNotNull().isGreaterThan(0);
    Assertions.assertThat(savedUser.getUsername()).isEqualTo("jean-sebastien");
    Assertions.assertThat(savedUser.getEmail()).isEqualTo("jean-sebastien@fake-email.fr");
    Assertions.assertThat(savedUser.getGender()).isEqualTo(Gender.MAN);
    Assertions.assertThat(savedUser.getPassword()).isNotNull();
  }

  @Test
  public void UserRepository_GetAll_ReturnMoreThanOneUser() {
    User user1 = User.builder()
      .username("jean-marc")
      .email("jean-marc@fake-email.fr")
      .gender(Gender.MAN)
      .password("Toto@2021*").build();

    User user2 = User.builder()
      .username("jean-baptiste")
      .email("jean-baptiste@fake-email.fr")
      .gender(Gender.MAN)
      .password("Toto@2021*").build();

    User savedUser1 = userRepository.save(user1);
    User savedUser2 = userRepository.save(user2);

    List<User> userList = userRepository.findAll();

    Assertions.assertThat(userList).isNotNull();
    Assertions.assertThat(userList.size()).isEqualTo(2);
  }
}
