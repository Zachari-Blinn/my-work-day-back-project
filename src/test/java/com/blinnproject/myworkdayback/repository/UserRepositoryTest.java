package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.AbstractIntegrationTest;
import com.blinnproject.myworkdayback.model.enums.EGender;
import com.blinnproject.myworkdayback.model.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTest extends AbstractIntegrationTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  @Order(value = 1)
  @DisplayName("Test connection to database")
  void testConnectionToDatabase() {
    org.junit.jupiter.api.Assertions.assertNotNull(userRepository);
  }

  @Test
  @Order(value = 2)
  @DisplayName("Test save user")
  void UserRepository_Save_ReturnSavedUser() {
    User user = User.builder()
      .username("jean-abernathy")
      .email("jean-abernathy@fake-email.fr")
      .gender(EGender.MAN)
      .password("Toto@2021*").build();

    User savedUser = userRepository.save(user);

    Assertions.assertThat(savedUser).isNotNull();
    Assertions.assertThat(savedUser.getId()).isNotNull().isPositive();
    Assertions.assertThat(savedUser.getUsername()).isEqualTo(user.getUsername());
    Assertions.assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
    Assertions.assertThat(savedUser.getGender()).isEqualTo(user.getGender());
    Assertions.assertThat(savedUser.getPassword()).isNotNull();
  }

  @Test
  @Order(value = 3)
  @DisplayName("Test get all users")
  void UserRepository_GetAll_ReturnMoreThanOneUser() {
    User user1 = User.builder()
      .username("jean-marc")
      .email("jean-marc@fake-email.fr")
      .gender(EGender.MAN)
      .password("Toto@2021*").build();

    User user2 = User.builder()
      .username("jean-barnabé")
      .email("jean-barnabe@fake-email.fr")
      .gender(EGender.MAN)
      .password("Toto@2021*").build();

    userRepository.save(user1);
    userRepository.save(user2);

    List<User> userList = userRepository.findAll();

    Assertions.assertThat(userList).isNotNull();
    Assertions.assertThat(userList).hasSizeGreaterThan(1);
  }

  @Test
  @Order(value = 4)
  @DisplayName("Test get user by username")
  void UserRepository_FindByUsername_ReturnUserByUsername() {
    User user1 = User.builder()
      .username("Jean-Adam")
      .email("Jean-Adam@fake-email.fr")
      .gender(EGender.MAN)
      .password("Toto@2021*").build();

    User user2 = User.builder()
      .username("Jean-Alexandre")
      .email("Jean-Alexandre@fake-email.fr")
      .gender(EGender.MAN)
      .password("Toto@2021*").build();

    userRepository.save(user1);
    userRepository.save(user2);

    Optional<User> userByUsername = userRepository.findByUsername(user2.getUsername());

    Assertions.assertThat(userByUsername).isPresent();
    Assertions.assertThat(userByUsername.get().getId()).isEqualTo(user2.getId());
    Assertions.assertThat(userByUsername.get().getUsername()).isEqualTo(user2.getUsername());
  }

  @Test
  @Order(value = 5)
  @DisplayName("Test get user by username with no user found")
  void UserRepository_FindByUsername_ReturnNoUserWithThisUsername() {
    User user1 = User.builder()
      .username("Jean-André")
      .email("Jean-Andre@fake-email.fr")
      .gender(EGender.MAN)
      .password("Toto@2021*").build();

    userRepository.save(user1);

    Optional<User> userByUsername = userRepository.findByUsername("non-existent-username");

    Assertions.assertThat(userByUsername).isEmpty();
  }

  @Test
  @Order(value = 6)
  @DisplayName("Test get user by email")
  void UserRepository_ExistsByUsername_ReturnTrueWithExistingUsername() {
    User user1 = User.builder()
      .username("Jean-Antoine")
      .email("Jean-Antoine@fake-email.fr")
      .gender(EGender.MAN)
      .password("Toto@2021*").build();

    userRepository.save(user1);

    Boolean userExist = userRepository.existsByUsername(user1.getUsername());

    Assertions.assertThat(userExist).isTrue();
  }

  @Test
  @Order(value = 7)
  @DisplayName("Test get user by email with no user found")
  void UserRepository_ExistsByUsername_ReturnFalseWithNoExistingUsername() {
    User user1 = User.builder()
      .username("Jean-Arthur")
      .email("Jean-Arthur@fake-email.fr")
      .gender(EGender.MAN)
      .password("Toto@2021*").build();

    userRepository.save(user1);

    Boolean userExist = userRepository.existsByUsername("non-existent-username");

    Assertions.assertThat(userExist).isFalse();
  }

  @Test
  @Order(value = 8)
  @DisplayName("Test get user by email")
  void UserRepository_ExistsByEmail_ReturnTrueWithExistingEmail() {
    User user1 = User.builder()
      .username("Jean-Augustin")
      .email("Jean-Augustin@fake-email.fr")
      .gender(EGender.MAN)
      .password("Toto@2021*").build();

    userRepository.save(user1);

    Boolean userExist = userRepository.existsByEmail(user1.getEmail());

    Assertions.assertThat(userExist).isTrue();
  }

  @Test
  @Order(value = 9)
  @DisplayName("Test get user by email with no user found")
  void UserRepository_ExistsByEmail_ReturnFalseWithNoExistingEmail() {
    User user1 = User.builder()
      .username("Jean-Auguste")
      .email("Jean-Auguste@fake-email.fr")
      .gender(EGender.MAN)
      .password("Toto@2021*").build();

    userRepository.save(user1);

    Boolean userExist = userRepository.existsByEmail("non-existent-email");

    Assertions.assertThat(userExist).isFalse();
  }
}
