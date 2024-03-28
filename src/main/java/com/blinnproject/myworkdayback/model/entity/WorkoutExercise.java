package com.blinnproject.myworkdayback.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "workout_exercise")
public class WorkoutExercise implements Serializable {

  @Serial
  private static final long serialVersionUID = 1905122041950251207L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Min(0)
  @Column(nullable = false)
  private int positionIndex;

  @ManyToOne
  @JoinColumn(name = "workout_model_id")
  private WorkoutModel workoutModel;

  @ManyToOne
  @JoinColumn(name = "workout_session_id")
  private WorkoutSession workoutSession;

  @ManyToOne
  @JoinColumn(name = "exercise_id")
  private Exercise exercise;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "workout_exercise_id")
  private List<WorkoutSet> workoutSets = new ArrayList<>();

  @Column(name = "notes")
  @Lob
  private String notes;

  @Column(name = "number_of_warm_up_sets")
  @Min(0)
  private int numberOfWarmUpSets;
}
