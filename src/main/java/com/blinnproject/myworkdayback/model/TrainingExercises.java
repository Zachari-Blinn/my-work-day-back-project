package com.blinnproject.myworkdayback.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class TrainingExercises {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "training_id")
  @JsonIgnore
  Training training;

  @ManyToOne
  @JoinColumn(name = "exercise_id")
  @JsonIgnore
  Exercise exercise;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "training_exercises_id")
  private List<Series> seriesList = new ArrayList<>();

  @Column
  private String notes;

  @Column
  private int numberOfWarmUpSeries;

  public TrainingExercises(Training training, Exercise exercise, String notes, int numberOfWarmUpSeries) {
    this.training = training;
    this.exercise = exercise;
    this.notes = notes;
    this.numberOfWarmUpSeries = numberOfWarmUpSeries;
  }

  public TrainingExercises() {

  }

  public void addSeries(Series series) {
    this.seriesList.add(series);
  }

  public void addSeriesList(List<Series> seriesList) {
    for (Series series : seriesList) {
      addSeries(series);
    }
  }
}
