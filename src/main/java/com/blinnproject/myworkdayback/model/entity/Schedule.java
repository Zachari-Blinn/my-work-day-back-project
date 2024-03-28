package com.blinnproject.myworkdayback.model.entity;

import com.blinnproject.myworkdayback.model.common.BaseEntityAudit;
import com.blinnproject.myworkdayback.model.enums.EDayOfWeek;
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
@Table(name = "schedule")
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
  private String frequency;

  @Column(name = "days_of_week")
  @Enumerated(EnumType.ORDINAL)
  private ArrayList<EDayOfWeek> daysOfWeek;

  @ManyToOne
  @JoinColumn(name = "workout_model_id")
  private WorkoutModel workoutModel;
}
