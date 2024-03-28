package com.blinnproject.myworkdayback.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "profile_picture")
public class ProfilePicture implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  private Long id;

  @MapsId
  @OneToOne
  private User user;

  @Column
  private String name;

  @Column
  private String type;

  @Lob
  @Column(length = 1000)
  private byte[] fileData;
}
