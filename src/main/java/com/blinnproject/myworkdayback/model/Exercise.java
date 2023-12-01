package com.blinnproject.myworkdayback.model;

import com.blinnproject.myworkdayback.model.common.BaseEntityAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Exercise extends BaseEntityAudit {

  @Column(length = 150, nullable = false)
  private String name;

//  @OneToMany(mappedBy = "exercise")
//  Set<TrainingExercises> trainingExercises;

  @ElementCollection(targetClass = EMuscle.class)
  @CollectionTable(name = "exercise_muscle", joinColumns = @JoinColumn(name = "exercise_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "muscle_name")
  private Set <EMuscle> musclesUsed;

}
