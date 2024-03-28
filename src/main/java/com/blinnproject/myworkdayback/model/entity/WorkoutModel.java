package com.blinnproject.myworkdayback.model.entity;

import com.blinnproject.myworkdayback.constraint.HexadecimalColorConstraint;
import com.blinnproject.myworkdayback.constraint.IconNameConstraint;
import com.blinnproject.myworkdayback.model.common.BaseEntityAudit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "workout_model")
public class WorkoutModel extends BaseEntityAudit {

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "sport_preset")
  private String sportPreset;

  @Column(name = "description")
  @Lob
  private String description;

  @Column(name = "has_warm_up", nullable = false)
  private Boolean hasWarmUp = true;

  @Column(name = "has_stretching", nullable = false)
  private Boolean hasStretching = false;

  @Column(name = "icon_name", nullable = false)
  @IconNameConstraint
  private String iconName = "icon_dumbbell";

  @Column(name = "icon_hexadecimal_color")
  @HexadecimalColorConstraint
  private String iconHexadecimalColor = "#0072db";
}
