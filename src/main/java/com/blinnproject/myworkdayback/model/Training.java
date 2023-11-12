package com.blinnproject.myworkdayback.model;

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
public class Training {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String sportPreset;

  private DayOfWeek[] trainingDays;

  private String description;

  private Boolean hasWarpUp;

  private Boolean hasStretching;

  @CreationTimestamp
  private Instant createdOn;

  @UpdateTimestamp
  private Instant lastUpdatedOn;

  @OneToMany(mappedBy = "training")
  private Set<SeriesExercise> seriesExercises;
}
