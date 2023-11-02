package com.blinnproject.myworkdayback.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Training {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreationTimestamp
  private Instant createdOn;

  @UpdateTimestamp
  private Instant lastUpdatedOn;

  @OneToMany(mappedBy = "training")
  private Set<SeriesExercise> seriesExercises;
}
