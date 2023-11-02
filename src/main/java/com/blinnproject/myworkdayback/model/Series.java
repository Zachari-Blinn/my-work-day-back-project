package com.blinnproject.myworkdayback.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Series {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "series_exercise_id", nullable = false)
  private SeriesExercise seriesExercise;

  @Min(0)
  @Max(150)
  private int weight;

  @Temporal(TemporalType.TIME)
  private Time restTime;

  @Lob
  private String notes;

  @Min(0)
  @Max(100)
  private int repsCount;

  private Difficulty difficulty;
}
