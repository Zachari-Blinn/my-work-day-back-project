package com.blinnproject.myworkdayback.model.entity;

import com.blinnproject.myworkdayback.constraint.HexadecimalColorConstraint;
import com.blinnproject.myworkdayback.constraint.IconNameConstraint;
import com.blinnproject.myworkdayback.model.enums.ESport;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("MODEL")
public class WorkoutModel extends Workout {

  @Column(name = "sport_preset")
  @Enumerated(EnumType.STRING)
  private ESport sportPreset;

  @Column(name = "has_warm_up", nullable = false)
  private Boolean hasWarmUp = true;

  @Column(name = "has_stretching", nullable = false)
  private Boolean hasStretching = false;

  @Column(name = "icon_name")
  @IconNameConstraint
  private String iconName = "icon_dumbbell";

  @Column(name = "icon_hexadecimal_color")
  @HexadecimalColorConstraint
  private String iconHexadecimalColor = "#0072db";

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "workout_model_id")
  private List<Schedule> schedules = new ArrayList<>();

  public WorkoutModel(String name, String description, String sportPreset, boolean hasWarmUp, boolean hasStretching, String icon_name, String icon_hexadecimal_color) {
    super(name, description);
    this.sportPreset = ESport.valueOf(sportPreset);
    this.hasWarmUp = hasWarmUp;
    this.hasStretching = hasStretching;
    this.iconName = icon_name;
    this.iconHexadecimalColor = icon_hexadecimal_color;
  }
}
