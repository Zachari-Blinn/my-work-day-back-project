package com.blinnproject.myworkdayback.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.*;

@Entity
@Getter
@Setter
public class TrainingExercises {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private TrainingExercises parent;

  @ManyToOne
  @JoinColumn(name = "training_id")
  private Training training;

  @ManyToOne
  @JoinColumn(name = "exercise_id")
  private Exercise exercise;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "training_exercises_id")
  private List<Series> seriesList = new ArrayList<>();

  @Column
  private String notes;

  @Column
  private int numberOfWarmUpSeries;

  @Temporal(TemporalType.DATE)
  private Date trainingDay;

  public TrainingExercises(Training training, Exercise exercise, String notes, int numberOfWarmUpSeries) {
    this.training = training;
    this.exercise = exercise;
    this.notes = notes;
    this.numberOfWarmUpSeries = numberOfWarmUpSeries;
  }

  public TrainingExercises(Training training, Exercise exercise, String notes, int numberOfWarmUpSeries, List<Series> seriesList) {
    this.training = training;
    this.exercise = exercise;
    this.notes = notes;
    this.numberOfWarmUpSeries = numberOfWarmUpSeries;
    this.seriesList.addAll(seriesList);
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

  // Constructor used for clone to new entity TrainingExercises and series associated
  public TrainingExercises(TrainingExercises that) {
    this(that.getTraining(), that.getExercise(), that.getNotes(), that.getNumberOfWarmUpSeries());

    List<Series> clonedSeriesList = new ArrayList<>();
    for (Series originalSeries : that.getSeriesList()) {
      Series clonedSeries = new Series(originalSeries);
      clonedSeriesList.add(clonedSeries);
    }
    this.setSeriesList(clonedSeriesList);
    this.setParent(that);
  }
}
