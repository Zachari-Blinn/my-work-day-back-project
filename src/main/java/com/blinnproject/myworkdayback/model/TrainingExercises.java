package com.blinnproject.myworkdayback.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
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

  @Column
  private String notes;

  @Column
  private int numberOfWarmUpSeries;

  @OneToMany(mappedBy = "trainingExercises", fetch = FetchType.EAGER)
  private Set<Series> series;

  public TrainingExercises(TrainingExercisesKey id, String notes, int numberOfWarmUpSeries) {
    this.id = id;
    this.notes = notes;
    this.numberOfWarmUpSeries = numberOfWarmUpSeries;
  }

  public TrainingExercises() {

  }

  public void addSeries(Set<Series> newSeries) {
    if (this.series == null) {
      this.series = new HashSet<>();
    }
    this.series.addAll(newSeries);
  }
}
