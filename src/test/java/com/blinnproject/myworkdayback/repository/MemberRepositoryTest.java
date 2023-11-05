package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.Gender;
import com.blinnproject.myworkdayback.model.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MemberRepositoryTest {

  @Autowired
  private MemberRepository memberRepository;

  @Test
  public void MemberRepository_SaveAll_ReturnSavedMember() {
    Member member = Member.builder()
      .username("jean-sebastien")
      .email("jean-sebastien@fake-email.fr")
      .gender(Gender.MAN)
      .password("Toto@2021*").build();

    Member savedMember = memberRepository.save(member);

    Assertions.assertThat(savedMember).isNotNull();
    Assertions.assertThat(savedMember.getId()).isNotNull().isGreaterThan(0);
    Assertions.assertThat(savedMember.getUsername()).isEqualTo("jean-sebastien");
    Assertions.assertThat(savedMember.getEmail()).isEqualTo("jean-sebastien@fake-email.fr");
    Assertions.assertThat(savedMember.getGender()).isEqualTo(Gender.MAN);
    Assertions.assertThat(savedMember.getPassword()).isNotNull();
  }

  @Test
  public void MemberRepository_GetAll_ReturnMoreThanOneMember() {
    Member member1 = Member.builder()
      .username("jean-marc")
      .email("jean-marc@fake-email.fr")
      .gender(Gender.MAN)
      .password("Toto@2021*").build();

    Member member2 = Member.builder()
      .username("jean-baptiste")
      .email("jean-baptiste@fake-email.fr")
      .gender(Gender.MAN)
      .password("Toto@2021*").build();

    Member savedMember1 = memberRepository.save(member1);
    Member savedMember2 = memberRepository.save(member2);

    List<Member> memberList = memberRepository.findAll();

    Assertions.assertThat(memberList).isNotNull();
    Assertions.assertThat(memberList.size()).isEqualTo(2);
  }
}
