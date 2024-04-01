package com.blinnproject.myworkdayback.model.entity;

import com.blinnproject.myworkdayback.model.enums.ESessionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("SESSION")
public class WorkoutSession extends Workout {

  @Column(name = "name")
  private String name;

  @ManyToOne
  @JoinColumn(name = "workout_model_id")
  private WorkoutModel workoutModel;

  @Column(name = "session_status")
  @Enumerated(EnumType.ORDINAL)
  private ESessionStatus sessionStatus = ESessionStatus.IN_PROGRESS;

  @Column(name = "started_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date startedAt;

  @Column(name = "ended_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date endedAt;
}
