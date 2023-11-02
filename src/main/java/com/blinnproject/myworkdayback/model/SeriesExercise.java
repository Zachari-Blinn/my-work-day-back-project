package com.blinnproject.myworkdayback.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class SeriesExercise {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "exercise_id", nullable = false)
  private Exercise exercise;

  @OneToMany(mappedBy = "seriesExercise")
  private Set<Series> series;

  @ManyToOne
  @JoinColumn(name = "training_id", nullable = false)
  private Training training;
}
