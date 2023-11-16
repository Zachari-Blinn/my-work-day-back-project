package com.blinnproject.myworkdayback.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Exercise extends BaseEntityAudit {

  @Column(length = 150, nullable = false)
  private String name;

  @Column(nullable = true)
  @OneToMany(mappedBy = "exercise")
  Set<TrainingExercises> trainingExercises;

  @ElementCollection(targetClass = Muscle.class)
  @CollectionTable(name = "exercise_muscle", joinColumns = @JoinColumn(name = "exercise_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "muscle_name")
  private Set <Muscle> musclesUsed;

}
