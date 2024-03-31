package com.blinnproject.myworkdayback.model.entity;

import com.blinnproject.myworkdayback.model.common.BaseEntityAudit;
import com.blinnproject.myworkdayback.model.enums.EDayOfWeek;
import com.blinnproject.myworkdayback.model.enums.EFrequency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SCHEDULE")
public class Schedule extends BaseEntityAudit {

  @Column(name = "start_date")
  @Temporal(TemporalType.DATE)
  private Date startDate;

  @Column(name = "end_date")
  @Temporal(TemporalType.DATE)
  private Date endDate;

  @Column(name = "start_time")
  @Temporal(TemporalType.TIME)
  private LocalTime startTime;

  @Column(name = "end_time")
  @Temporal(TemporalType.TIME)
  private LocalTime endTime;

  @Column(name = "frequency")
  @Enumerated(EnumType.ORDINAL)
  private EFrequency frequency;

  @Column(name = "monday")
  private Boolean monday;

  @Column(name = "tuesday")
  private Boolean tuesday;

  @Column(name = "wednesday")
  private Boolean wednesday;

  @Column(name = "thursday")
  private Boolean thursday;

  @Column(name = "friday")
  private Boolean friday;

  @Column(name = "saturday")
  private Boolean saturday;

  @Column(name = "sunday")
  private Boolean sunday;
}
