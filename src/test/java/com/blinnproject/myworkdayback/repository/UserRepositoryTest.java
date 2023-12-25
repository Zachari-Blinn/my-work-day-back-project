package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.EGender;
import com.blinnproject.myworkdayback.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

@SpringBootTest()
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  public void UserRepository_SaveAll_ReturnSavedUser() {
    User user = User.builder()
      .username("jean-abernathy")
      .email("jean-abernathy@fake-email.fr")
      .gender(EGender.MAN)
      .password("Toto@2021*").build();

    User savedUser = userRepository.save(user);

    Assertions.assertThat(savedUser).isNotNull();
    Assertions.assertThat(savedUser.getId()).isNotNull().isGreaterThan(0);
    Assertions.assertThat(savedUser.getUsername()).isEqualTo(user.getUsername());
    Assertions.assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
    Assertions.assertThat(savedUser.getGender()).isEqualTo(user.getGender());
    Assertions.assertThat(savedUser.getPassword()).isNotNull();
  }

  @Test
  public void UserRepository_GetAll_ReturnMoreThanOneUser() {
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
    Assertions.assertThat(userList.size()).isGreaterThan(1);
  }

  @Test
  public void UserRepository_FindByUsername_ReturnUserByUsername() {
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

    Assertions.assertThat(userByUsername.isPresent()).isTrue();
    Assertions.assertThat(userByUsername.get().getId()).isEqualTo(user2.getId());
    Assertions.assertThat(userByUsername.get().getUsername()).isEqualTo(user2.getUsername());
  }

  @Test
  public void UserRepository_FindByUsername_ReturnNoUserWithThisUsername() {
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
  public void UserRepository_ExistsByUsername_ReturnTrueWithExistingUsername() {
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
  public void UserRepository_ExistsByUsername_ReturnFalseWithNoExistingUsername() {
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
  public void UserRepository_ExistsByEmail_ReturnTrueWithExistingEmail() {
    User user1 = User.builder()
      .username("Jean-Augustin")
      .email("Jean-Antoine@fake-email.fr")
      .gender(EGender.MAN)
      .password("Toto@2021*").build();

    userRepository.save(user1);

    Boolean userExist = userRepository.existsByEmail(user1.getEmail());

    Assertions.assertThat(userExist).isTrue();
  }

  @Test
  public void UserRepository_ExistsByEmail_ReturnFalseWithNoExistingEmail() {
    User user1 = User.builder()
      .username("Jean-Auguste")
      .email("Jean-Arthur@fake-email.fr")
      .gender(EGender.MAN)
      .password("Toto@2021*").build();

    userRepository.save(user1);

    Boolean userExist = userRepository.existsByEmail("non-existent-email");

    Assertions.assertThat(userExist).isFalse();
  }
}
