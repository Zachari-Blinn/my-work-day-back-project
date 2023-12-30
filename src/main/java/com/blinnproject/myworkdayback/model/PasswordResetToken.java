package com.blinnproject.myworkdayback.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PasswordResetToken {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @Column(nullable = false)
  private String token;

  @Column(nullable = false)
  private Date expiryDate = new Date(System.currentTimeMillis() + 1800000);

  @Column()
  private int attempts = 0;

  public PasswordResetToken(String token, User user) {
    this.token = token;
    this.user = user;
  }
}
