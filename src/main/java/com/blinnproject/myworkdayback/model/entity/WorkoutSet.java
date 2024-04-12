package com.blinnproject.myworkdayback.model.entity;

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
@Table(name = "WORKOUT_SETS")
public class WorkoutSet extends BaseEntityAudit {
  @Min(0)
  @Column(name = "position_index", nullable = false)
  private int positionIndex;

  @Column(name = "is_performed")
  private Boolean isPerformed = false;

  @Column(name = "weight")
  @Min(0)
  @Max(150)
  private int weight;

  @Column(name = "rest_time")
  private String restTime;

  @Column(name = "notes")
  private String notes;

  @Column(name = "reps_count")
  @Min(0)
  @Max(100)
  private int repsCount;
}
