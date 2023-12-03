package com.blinnproject.myworkdayback.model;

import com.blinnproject.myworkdayback.model.common.BaseEntityAudit;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Series extends BaseEntityAudit implements Cloneable {
  @Min(0)
  @Column(nullable = false)
  private int positionIndex;

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

  public Series(Series that) {
    this(that.getPositionIndex(), that.getWeight(), that.getRestTime(), that.getNotes(), that.getRepsCount(), that.getDifficulty());
  }

  @Override
  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException("Error cloning Series", e);
    }
  }
}
