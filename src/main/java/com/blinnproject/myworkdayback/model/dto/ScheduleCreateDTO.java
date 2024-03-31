package com.blinnproject.myworkdayback.model.dto;

import com.blinnproject.myworkdayback.model.enums.EFrequency;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

/**
 * DTO for {@link com.blinnproject.myworkdayback.model.entity.Schedule}
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScheduleCreateDTO {
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalTime startTime;
  private LocalTime endTime;
  private EFrequency frequency;
  private Boolean monday;
  private Boolean tuesday;
  private Boolean wednesday;
  private Boolean thursday;
  private Boolean friday;
  private Boolean saturday;
  private Boolean sunday;
}