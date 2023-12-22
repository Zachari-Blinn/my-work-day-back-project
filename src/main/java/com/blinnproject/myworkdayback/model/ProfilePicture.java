package com.blinnproject.myworkdayback.model;

import com.blinnproject.myworkdayback.model.common.BaseEntityAudit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfilePicture {
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
