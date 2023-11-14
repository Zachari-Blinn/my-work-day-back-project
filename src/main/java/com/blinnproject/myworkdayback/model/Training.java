package com.blinnproject.myworkdayback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

  @Column(nullable = false)
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

  @ManyToOne()
  @JoinColumn(name = "user_id", nullable = true)
  @JsonIgnore
  private User user;

  public Training(String name, String sportPreset, DayOfWeek[] trainingDays, String description, Boolean hasWarpUp, Boolean hasStretching, User user) {
    this.name = name;
    this.sportPreset = sportPreset;
    this.trainingDays = trainingDays;
    this.description = description;
    this.hasWarpUp = hasWarpUp;
    this.hasStretching = hasStretching;
    this.user = user;
  }
}
