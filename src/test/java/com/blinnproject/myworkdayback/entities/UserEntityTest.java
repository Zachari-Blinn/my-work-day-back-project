package com.blinnproject.myworkdayback.entities;

import com.blinnproject.myworkdayback.model.entity.User;
import com.blinnproject.myworkdayback.model.enums.EGender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserEntityTest {

  @Test
  void testCreateUser() {
    User user = new User();

    user.setUsername("oTHEThuB");
    user.setGender(EGender.WOMAN);
    user.setPassword("Toto@123*");
    user.setEmail("oTHEThuB@fake-email.fr");

    Assertions.assertEquals("oTHEThuB", user.getUsername());
    Assertions.assertEquals(EGender.WOMAN, user.getGender());
    Assertions.assertEquals("Toto@123*", user.getPassword());
    Assertions.assertEquals("oTHEThuB@fake-email.fr", user.getEmail());
  }

}
