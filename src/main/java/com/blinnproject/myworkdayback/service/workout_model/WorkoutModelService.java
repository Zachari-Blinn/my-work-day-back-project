package com.blinnproject.myworkdayback.service.workout_model;

import com.blinnproject.myworkdayback.model.dto.ScheduleCreateDTO;
import com.blinnproject.myworkdayback.model.dto.WorkoutExerciseCreateDTO;
import com.blinnproject.myworkdayback.model.dto.WorkoutModelCreateDTO;
import com.blinnproject.myworkdayback.model.dto.WorkoutModelSessionDateDTO;
import com.blinnproject.myworkdayback.model.entity.Schedule;
import com.blinnproject.myworkdayback.model.entity.WorkoutExercise;
import com.blinnproject.myworkdayback.model.entity.WorkoutModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface WorkoutModelService {
  WorkoutModel create(WorkoutModelCreateDTO workoutModelCreateDTO, Long createdBy);

  Optional<WorkoutModel> findById(Long id, Long createdBy);

  WorkoutExercise addExercise(Long workoutModelId, Long exerciseId, WorkoutExerciseCreateDTO workoutExerciseCreateDTO, Long createdBy);

  WorkoutExercise removeExercise(Long workoutExerciseId, Long createdBy);

  Optional<WorkoutModel> delete(Long id, Long createdBy);

  ArrayList<WorkoutModel> findAll(Long createdBy);

  Optional<WorkoutModel> update(Long id, WorkoutModelCreateDTO workoutModelCreateDTO, Long createdBy);

  ArrayList<WorkoutExercise> findAllExercises(Long id, Long createdBy);

  Schedule addSchedule(Long workoutModelId, ScheduleCreateDTO scheduleCreateDTO, Long createdBy);

  void removeSchedule(Long scheduleId, Long createdBy);

  List<WorkoutModelSessionDateDTO> getAllWorkoutModelPlanSessionsByDate(LocalDate startDate, LocalDate endDate, Long createdBy);
}
