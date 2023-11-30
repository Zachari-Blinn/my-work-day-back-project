package com.blinnproject.myworkdayback.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
  @EmbeddedId
  TrainingExercisesKey id = new TrainingExercisesKey();

  @ManyToOne
  @JsonBackReference(value = "training-exercises-training")
  @MapsId("trainingId")
  @JoinColumn(name = "training_id")
  Training training;

  @ManyToOne
  @JsonBackReference(value = "training-exercises-exercise")
  @MapsId("exerciseId")
  @JoinColumn(name = "exercise_id")
  Exercise exercise;

  @OneToMany(mappedBy = "trainingExercises", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<Series> seriesList = new ArrayList<>();

  @Column
  private String notes;

  @Column
  private int numberOfWarmUpSeries;

  public TrainingExercises(TrainingExercisesKey id, String notes, int numberOfWarmUpSeries) {
    this.id = id;
    this.notes = notes;
    this.numberOfWarmUpSeries = numberOfWarmUpSeries;
  }

  public TrainingExercises() {

  }

  public void addSeries(Series series) {
    this.seriesList.add(series);
    series.setTrainingExercises(this);
  }

  public void addSeriesList(List<Series> seriesList) {
    for (Series series : seriesList) {
      addSeries(series);
    }
  }

}
