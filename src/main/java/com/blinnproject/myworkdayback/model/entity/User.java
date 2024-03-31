package com.blinnproject.myworkdayback.model.entity;

import com.blinnproject.myworkdayback.model.common.BaseEntityAudit;
import com.blinnproject.myworkdayback.model.entity.ProfilePicture;
import com.blinnproject.myworkdayback.model.entity.Role;
import com.blinnproject.myworkdayback.model.enums.EGender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS",
  uniqueConstraints = {
    @UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "email")
  })
public class User extends BaseEntityAudit {
  @Column(nullable=false, length = 20)
  private String username;

  @Enumerated(EnumType.ORDINAL)
  private EGender gender;

  @Email(flags = Pattern.Flag.CASE_INSENSITIVE)
  @Column(nullable=false, unique=true)
  private String email;

  @Column(nullable=false)
  private String password;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @PrimaryKeyJoinColumn
  private ProfilePicture profilePicture;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  public User(String username, String email, EGender gender, String password) {
    this.username = username;
    this.email = email;
    this.gender = gender;
    this.password = password;
  }
}

