package com.blinnproject.myworkdayback.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false, length = 50)
  private String username;

  @Enumerated(EnumType.ORDINAL)
  private Gender gender;

  @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
    flags = Pattern.Flag.CASE_INSENSITIVE)
  @Column(nullable=false, unique=true)
  private String email;

  @Column(nullable=false)
  private String password;

  @CreationTimestamp
  private Instant createdOn;

  @UpdateTimestamp
  private Instant lastUpdatedOn;
}

