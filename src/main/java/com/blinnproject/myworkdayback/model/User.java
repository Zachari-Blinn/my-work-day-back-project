package com.blinnproject.myworkdayback.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users",
  uniqueConstraints = {
    @UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "email")
  })
public class User extends BaseEntityAudit {
  @Column(nullable=false, length = 20)
  private String username;

  @Column(nullable=false)
  @Enumerated(EnumType.ORDINAL)
  private Gender gender = Gender.NOT_SPECIFIED;

  @Email(flags = Pattern.Flag.CASE_INSENSITIVE)
  @Column(nullable=false, unique=true)
  private String email;

  @Column(nullable=false)
  private String password;

  @Column(nullable=false)
  @Enumerated(EnumType.STRING)
  private Role role = Role.ROLE_USER;

  @Column(nullable=false)
  @Enumerated(EnumType.STRING)
  private Provider provider = Provider.LOCAL;

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }
}

