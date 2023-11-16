package com.blinnproject.myworkdayback.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class TrainingExercises {
  @EmbeddedId
  TrainingExercisesKey id;

  @ManyToOne
  @MapsId("trainingId")
  @JoinColumn(name = "training_id")
  Training training;

  @ManyToOne
  @MapsId("exerciseId")
  @JoinColumn(name = "exercise_id")
  Exercise exercise;

  private String notes;

  @OneToMany(mappedBy = "trainingExercises")
  private Set<Series> series;
}
