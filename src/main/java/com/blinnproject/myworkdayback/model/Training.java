package com.blinnproject.myworkdayback.model;

import com.blinnproject.myworkdayback.model.common.BaseEntityAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
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

  @Column(nullable = false)
  private Boolean hasWarpUp = true;

  @Column(nullable = false)
  private Boolean hasStretching = false;

  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private ETrainingStatus trainingStatus = ETrainingStatus.IN_PROGRESS;

  @Column(nullable = false)
  private String iconName = "dumbbell";

  @Column
  @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
  private String iconHexadecimalColor = "#0072db";
}
