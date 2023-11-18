package com.blinnproject.myworkdayback.model;

import com.blinnproject.myworkdayback.model.common.BaseEntityAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Series extends BaseEntityAudit {
  @Min(0)
  @Column(nullable = false)
  private int positionIndex;

  @ManyToOne
  @JoinColumns({
    @JoinColumn(name = "training_id", referencedColumnName = "training_id"),
    @JoinColumn(name = "exercise_id", referencedColumnName = "exercise_id")
  })
  private TrainingExercises trainingExercises;

  @Min(0)
  @Max(150)
  private int weight;

  private String restTime;

  @Lob
  private String notes;

  @Min(0)
  @Max(100)
  private int repsCount;

  private EDifficulty difficulty;
}
