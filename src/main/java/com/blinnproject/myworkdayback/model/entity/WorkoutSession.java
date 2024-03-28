package com.blinnproject.myworkdayback.model.entity;

import com.blinnproject.myworkdayback.model.common.BaseEntityAudit;
import com.blinnproject.myworkdayback.model.enums.ESessionStatus;
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
@Table(name = "workout_session")
public class WorkoutSession extends BaseEntityAudit {

  @Column(name = "name", nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "workout_model_id")
  private WorkoutModel workoutModel;

  @Column(name = "session_status")
  @Enumerated(EnumType.ORDINAL)
  private ESessionStatus sessionStatus;

  @Column(name = "started_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date startedAt;

  @Column(name = "ended_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date endedAt;

  @Column(name = "description")
  @Lob
  private String description;

  @OneToMany(mappedBy = "workout_session", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<WorkoutExercise> workoutExerciseList = new ArrayList<>();
}
