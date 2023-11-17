package com.blinnproject.myworkdayback.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class TrainingExercisesKey implements Serializable {
  @Column(name = "training_id")
  Long trainingId;

  @Column(name = "exercise_id")
  Long exerciseId;

  public TrainingExercisesKey() {}

  public TrainingExercisesKey(Long trainingId, Long exerciseId) {
    super();
    this.trainingId = trainingId;
    this.exerciseId = exerciseId;
  }
}
