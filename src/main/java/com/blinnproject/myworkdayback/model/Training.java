package com.blinnproject.myworkdayback.model;

import com.blinnproject.myworkdayback.model.common.BaseEntityAudit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.DayOfWeek;
import java.util.ArrayList;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Training extends BaseEntityAudit {

  @Column(nullable = false)
  private String name;

  @Column
  private String sportPreset;

  @Column
  private ArrayList<DayOfWeek> trainingDays;

  @Lob
  private String description;

  @Column
  private Boolean hasWarpUp;

  @Column
  private Boolean hasStretching;
}
