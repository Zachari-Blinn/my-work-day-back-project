package com.blinnproject.myworkdayback.model;

import com.blinnproject.myworkdayback.model.common.BaseEntityAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Training extends BaseEntityAudit {

  @Column(nullable = false)
  private String name;

  @Column
  private String sportPreset;

  @Column
  private ArrayList<DayOfWeek> trainingDays;

  @Column
  @Enumerated(EnumType.ORDINAL)
  private ETrainingStatus trainingStatus;

  @Column
  @Temporal(TemporalType.DATE)
  private Date startDate;

  @Column
  @Temporal(TemporalType.DATE)
  private Date endDate;

  @Temporal(TemporalType.DATE)
  private Date performedDate;

  @Lob
  private String description;

  @Column(nullable = false)
  private Boolean hasWarpUp = true;

  @Column(nullable = false)
  private Boolean hasStretching = false;

  @Column(nullable = false)
  private String iconName = "dumbbell";

  @Column
  @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
  private String iconHexadecimalColor = "#0072db";

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(referencedColumnName = "id")
  private Training parent;

  // Constructor used for clone to new entity Training
  public Training(Training that) {
    this(that.getName(), that.getSportPreset(), that.getTrainingDays(), that.getTrainingStatus(), that.getStartDate(), that.getEndDate(),that.getPerformedDate(), that.getDescription(), that.getHasWarpUp(), that.getHasStretching(), that.getIconName(), that.getIconHexadecimalColor(), null);

    this.setParent(that);
  }
}
