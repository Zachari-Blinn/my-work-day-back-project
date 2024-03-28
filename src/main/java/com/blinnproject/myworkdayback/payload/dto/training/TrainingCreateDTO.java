package com.blinnproject.myworkdayback.payload.dto.training;

import com.blinnproject.myworkdayback.constraint.HexadecimalColorConstraint;
import com.blinnproject.myworkdayback.constraint.IconNameConstraint;
import com.blinnproject.myworkdayback.model.enums.EDayOfWeek;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.ArrayList;
import java.util.Date;

@Data
public class TrainingCreateDTO {
  @NotBlank
  @Size(min = 1, max = 50)
  private String name;

  private String sportPreset;

  private ArrayList<EDayOfWeek> trainingDays;

  private Date startDate;

  private Date endDate;

  private String description;

  private Boolean hasWarmUp;

  private Boolean hasStretching;

  @NotBlank
  @IconNameConstraint
  private String iconName;

  @HexadecimalColorConstraint
  private String iconHexadecimalColor;
}
