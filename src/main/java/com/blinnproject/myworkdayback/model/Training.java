package com.blinnproject.myworkdayback.model;

import com.blinnproject.myworkdayback.constraint.HexadecimalColorConstraint;
import com.blinnproject.myworkdayback.constraint.IconNameConstraint;
import com.blinnproject.myworkdayback.model.common.BaseEntityAudit;
import com.blinnproject.myworkdayback.payload.dto.training.TrainingCreateDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "training")
public class Training extends BaseEntityAudit {

  @Column(nullable = false)
  private String name;

  @Column
  private String sportPreset;

  @Column
  @Enumerated(EnumType.ORDINAL)
  private ArrayList<EDayOfWeek> trainingDays;

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
  private Boolean hasWarmUp = true;

  @Column(nullable = false)
  private Boolean hasStretching = false;

  @Column(nullable = false)
  @IconNameConstraint
  private String iconName = "icon_dumbbell";

  @Column
  @HexadecimalColorConstraint
  private String iconHexadecimalColor = "#0072db";

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(referencedColumnName = "id")
  private Training parent;

  @OneToMany(mappedBy = "training", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<TrainingExercises> trainingExercisesList = new ArrayList<>();

  // Constructor used for clone to new entity Training
  public Training(Training that) {
    this(that.getName(), that.getSportPreset(), that.getTrainingDays(), that.getTrainingStatus(), that.getStartDate(), that.getEndDate(),that.getPerformedDate(), that.getDescription(), that.gethasWarmUp(), that.getHasStretching(), that.getIconName(), that.getIconHexadecimalColor(), null, null);

    this.setParent(that);
  }

  public Training(TrainingCreateDTO that) {
    this(that.getName(), that.getSportPreset(), that.getTrainingDays(), null, that.getStartDate(), that.getEndDate(), null, that.getDescription(), that.gethasWarmUp(), that.getHasStretching(), that.getIconName(), that.getIconHexadecimalColor(), null, null);
  }
}
