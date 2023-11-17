package com.blinnproject.myworkdayback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.DayOfWeek;
import java.time.Instant;
import java.util.Set;

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
  private DayOfWeek[] trainingDays;

  @Column
  private String description;

  @Column
  private Boolean hasWarpUp;

  @Column
  private Boolean hasStretching;

  @OneToMany(mappedBy = "training")
  @JsonManagedReference(value = "training-exercises-training")
  Set<TrainingExercises> trainingExercises;

  @ManyToOne()
  @JoinColumn(name = "user_id", nullable = true)
  @JsonIgnore
  private User user;

}
