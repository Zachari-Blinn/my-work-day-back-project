package com.blinnproject.myworkdayback.model.entity;

import com.blinnproject.myworkdayback.model.common.BaseEntityAudit;
import com.blinnproject.myworkdayback.model.enums.EDifficulty;
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
@Table(name = "series")
public class Series extends BaseEntityAudit {
  @Min(0)
  @Column(nullable = false)
  private int positionIndex;

  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private Series parent;

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
    this(that.getPositionIndex(), null, that.getWeight(), that.getRestTime(), that.getNotes(), that.getRepsCount(), that.getDifficulty());
    this.setParent(that);
  }

}
