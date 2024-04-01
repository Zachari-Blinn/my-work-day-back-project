package com.blinnproject.myworkdayback.model.entity;

import com.blinnproject.myworkdayback.model.common.BaseEntityAudit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "WORKOUT")
@DiscriminatorColumn(name = "workout_type")
public class Workout extends BaseEntityAudit {
  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "workout")
  private List<WorkoutExercise> workoutExerciseList;
}
