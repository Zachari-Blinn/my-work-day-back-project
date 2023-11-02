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
public class Exercise {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 150)
  private String name;

  @OneToMany(mappedBy = "exercise")
  private Set<SeriesExercise> seriesExercises;

  @ManyToMany
  private Set<Muscle> musclesUsed;
}
