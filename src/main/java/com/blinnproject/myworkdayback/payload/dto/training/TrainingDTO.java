package com.blinnproject.myworkdayback.payload.dto.training;

import com.blinnproject.myworkdayback.constraint.HexadecimalColorConstraint;
import com.blinnproject.myworkdayback.constraint.IconNameConstraint;
import com.blinnproject.myworkdayback.model.EDayOfWeek;
import com.blinnproject.myworkdayback.model.ETrainingStatus;
import com.blinnproject.myworkdayback.model.Training;
import com.blinnproject.myworkdayback.model.TrainingExercises;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
import lombok.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class TrainingDTO {
  @Id
  @NotNull
  private Long id;

  @NotBlank
  @Size(min = 1, max = 50)
  private String name;

  private String sportPreset;

  private ArrayList<EDayOfWeek> trainingDays;

  private ETrainingStatus trainingStatus;

  private Date startDate;

  private Date endDate;

  private Date performedDate;

  private String description;

  @BooleanFlag
  private Boolean hasWarpUp;

  private Boolean hasStretching;

  @NotBlank
  @IconNameConstraint
  private String iconName;

  @HexadecimalColorConstraint
  private String iconHexadecimalColor;

  private Training parent;

  @JsonIgnore
  private List<TrainingExercises> trainingExercisesList;
}
