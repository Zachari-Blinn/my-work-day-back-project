package com.blinnproject.myworkdayback.model.entity;

import com.blinnproject.myworkdayback.model.common.BaseEntityAudit;
import com.blinnproject.myworkdayback.model.dto.WorkoutExerciseCreateDTO;
import com.blinnproject.myworkdayback.model.enums.EPerformStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "WORKOUT_EXERCISE")
public class WorkoutExercise  extends BaseEntityAudit {
  @Min(0)
  @Column(nullable = false)
  private int positionIndex;

  @Column(name = "perform_status", nullable = true)
  private EPerformStatus performStatus;

  @ManyToOne()
  @JoinColumn(name = "workout_id")
  @JsonIgnore
  private Workout workout;

  @ManyToOne
  @JoinColumn(name = "exercise_id")
  private Exercise exercise;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "workout_exercise_id")
  private List<WorkoutSet> workoutSets = new ArrayList<>();

  @Column(name = "notes")
  private String notes;

  @Column(name = "number_of_warm_up_sets")
  @Min(0)
  private int numberOfWarmUpSets;

  // Constructors

  public WorkoutExercise() {
  }

  public WorkoutExercise(WorkoutModel workoutModel, Exercise exercise, WorkoutExerciseCreateDTO that) {
    this(workoutModel, exercise, that.getPositionIndex(), that.getNotes(), that.getNumberOfWarmUpSets());
    this.addWorkoutSets(that.getWorkoutSets());
  }

  public WorkoutExercise(Workout workout, Exercise exercise, int positionIndex, String notes, int numberOfWarmUpSets) {
    this.workout = workout;
    this.exercise = exercise;
    this.positionIndex = positionIndex;
    this.notes = notes;
    this.numberOfWarmUpSets = numberOfWarmUpSets;
  }

  @PrePersist
  public void prePersist() {
    if (this.getWorkout() instanceof WorkoutModel) {
      this.setPerformStatus(EPerformStatus.NOT_PERFORMED);
    }
  }

  // methods

  public void addWorkoutSets(List<WorkoutSet> providedSetsList) {
    if (providedSetsList != null) {
      int totalNumberOfSeries = this.workoutSets.size() + providedSetsList.size();
      for (WorkoutSet workoutSet : providedSetsList) {
        workoutSet.setPositionIndex(this.determinePositionIndex(workoutSet.getPositionIndex(), totalNumberOfSeries));

        this.workoutSets.add(workoutSet);
      }
    }
  }

  private int determinePositionIndex(int currentIndex, int total) {
    // Si la série ne contient pas de valeur positionIndex, attribuez-lui une nouvelle valeur
    if (currentIndex == 0) {
      return this.workoutSets.size() + 1;
    } else {
      // Vérifiez si la valeur positionIndex est déjà utilisée
      if (this.workoutSets.stream().anyMatch(s -> s.getPositionIndex() == currentIndex)) {
        throw new IllegalArgumentException("positionIndex already used");
      }

      // Vérifiez que la valeur positionIndex est dans la plage autorisée
      if (currentIndex < 1 || currentIndex > total) {
        throw new IllegalArgumentException("positionIndex must be a number between 1 and " + (this.workoutSets.size() + 1));
      }

      return currentIndex;
    }
  }
}
