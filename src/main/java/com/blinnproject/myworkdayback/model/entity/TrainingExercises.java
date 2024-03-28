package com.blinnproject.myworkdayback.model.entity;

import com.blinnproject.myworkdayback.payload.dto.training_exercises.TrainingExercisesCreateDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "training_exercises")
public class TrainingExercises implements Serializable {

  @Serial
  private static final long serialVersionUID = 1905122041950251207L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Min(0)
  @Column(nullable = false)
  private int positionIndex;

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
  @Min(0)
  private int numberOfWarmUpSeries;

  @Temporal(TemporalType.DATE)
  private Date trainingDay;

  public TrainingExercises(Training training, Exercise exercise, int positionIndex, String notes, int numberOfWarmUpSeries) {
    this.training = training;
    this.exercise = exercise;
    this.positionIndex = positionIndex;
    this.notes = notes;
    this.numberOfWarmUpSeries = numberOfWarmUpSeries;
  }

  public TrainingExercises(Training training, Exercise exercise, int positionIndex, String notes, int numberOfWarmUpSeries, List<Series> seriesList) {
    this.training = training;
    this.exercise = exercise;
    this.positionIndex = positionIndex;
    this.notes = notes;
    this.numberOfWarmUpSeries = numberOfWarmUpSeries;
    this.seriesList.addAll(seriesList);
  }

  public TrainingExercises() {

  }

  public void addSeriesList(List<Series> providedSeriesList) {
    int totalNumberOfSeries = this.seriesList.size() + providedSeriesList.size();
    for (Series series : providedSeriesList) {
      series.setPositionIndex(this.determinePositionIndex(series.getPositionIndex(), totalNumberOfSeries));

      this.seriesList.add(series);
    }
  }

  // Constructor used for clone to new entity TrainingExercises and series associated
  public TrainingExercises(TrainingExercises that) {
    this(that.getTraining(), that.getExercise(), that.getPositionIndex(), that.getNotes(), that.getNumberOfWarmUpSeries());

    List<Series> clonedSeriesList = new ArrayList<>();

    for (Series originalSeries : that.getSeriesList()) {
      Series clonedSeries = new Series(originalSeries);
      clonedSeriesList.add(clonedSeries);
    }

    this.setSeriesList(clonedSeriesList);
    this.setParent(that);
  }

  public TrainingExercises(Training training, Exercise exercise, TrainingExercisesCreateDTO that) {
    this(training, exercise, that.getPositionIndex(), that.getNotes(), that.getNumberOfWarmUpSeries());
    this.addSeriesList(that.getSeries());
  }

  private int determinePositionIndex(int currentIndex, int total) {
    // Si la série ne contient pas de valeur positionIndex, attribuez-lui une nouvelle valeur
    if (currentIndex == 0) {
      return this.seriesList.size() + 1;
    } else {
      // Vérifiez si la valeur positionIndex est déjà utilisée
      if (this.seriesList.stream().anyMatch(s -> s.getPositionIndex() == currentIndex)) {
        throw new IllegalArgumentException("positionIndex already used");
      }

      // Vérifiez que la valeur positionIndex est dans la plage autorisée
      if (currentIndex < 1 || currentIndex > total) {
        throw new IllegalArgumentException("positionIndex must be a number between 1 and " + (this.seriesList.size() + 1));
      }

      return currentIndex;
    }
  }
}
