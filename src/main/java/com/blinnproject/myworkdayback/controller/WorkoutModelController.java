package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.model.dto.ScheduleCreateDTO;
import com.blinnproject.myworkdayback.model.dto.WorkoutExerciseCreateDTO;
import com.blinnproject.myworkdayback.model.dto.WorkoutModelCreateDTO;
import com.blinnproject.myworkdayback.model.entity.Schedule;
import com.blinnproject.myworkdayback.model.entity.WorkoutExercise;
import com.blinnproject.myworkdayback.model.entity.WorkoutModel;
import com.blinnproject.myworkdayback.model.response.GenericResponse;
import com.blinnproject.myworkdayback.security.UserDetailsImpl;
import com.blinnproject.myworkdayback.service.i18n.I18nService;
import com.blinnproject.myworkdayback.service.mapper.WorkoutModelMapper;
import com.blinnproject.myworkdayback.service.workout_model.WorkoutModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Tag(name="Workout model", description = "Endpoints related to user workout model.")
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/api/workout-model", produces="application/json")
public class WorkoutModelController {

  private final I18nService i18n;
  private final WorkoutModelService workoutModelService;

  public WorkoutModelController(I18nService i18nService, WorkoutModelService workoutModelService, WorkoutModelMapper workoutModelMapper) {
    this.i18n = i18nService;
    this.workoutModelService = workoutModelService;
  }

  @Operation(summary = "Create workout model", description = "Creates a new workout model.")
  @PostMapping()
  public ResponseEntity<GenericResponse<WorkoutModel>> create(@Valid @RequestBody WorkoutModelCreateDTO workoutModelCreateDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    WorkoutModel workoutModelResponse = workoutModelService.create(workoutModelCreateDTO, userDetails.getId());

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(GenericResponse.success(workoutModelResponse, i18n.translate("controller.workout.model.create.successful")));
  }

  @Operation(summary = "Get workout model by ID", description = "Retrieves a workout model by its ID.")
  @GetMapping("/{id}")
  public ResponseEntity<GenericResponse<WorkoutModel>> getWorkoutModelById(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Optional<WorkoutModel> workoutModel = workoutModelService.findById(id, userDetails.getId());

    return workoutModel.map(model -> ResponseEntity.ok(GenericResponse.success(model, i18n.translate("controller.workout.model.return-by-id.successful")))).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @Operation(summary = "Add Exercise to Workout model", description = "Add an exercise to a workout model.")
  @PostMapping("/{workoutModelId}/exercise/{exerciseId}")
  public ResponseEntity<GenericResponse<WorkoutExercise>> addExerciseToWorkoutModel(
      @PathVariable("workoutModelId") Long workoutModelId,
      @PathVariable("exerciseId") Long exerciseId,
      @RequestBody WorkoutExerciseCreateDTO workoutExerciseCreateDTO,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    WorkoutExercise createdTrainingExercises = this.workoutModelService.addExercise(workoutModelId, exerciseId, workoutExerciseCreateDTO, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(createdTrainingExercises, i18n.translate("controller.workout.model.add-exercise.successful")));
  }

  @Operation(summary = "Remove Exercise from Workout model", description = "Remove an exercise from a workout model with exercise ID.")
  @DeleteMapping("/exercise/{workoutExerciseId}")
  public ResponseEntity<GenericResponse<WorkoutExercise>> removeExerciseFromWorkoutModel(
      @PathVariable("workoutExerciseId") Long workoutExerciseId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    WorkoutExercise removedTrainingExercises = this.workoutModelService.removeExercise(workoutExerciseId, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(removedTrainingExercises, i18n.translate("controller.workout.model.remove-exercise.successful")));
  }

  @Operation(summary = "Delete workout model", description = "Deletes a workout model by its ID.")
  @DeleteMapping("/{id}")
  public ResponseEntity<GenericResponse<WorkoutModel>> deleteWorkoutModel(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Optional<WorkoutModel> workoutModel = workoutModelService.delete(id, userDetails.getId());

    return workoutModel.map(model -> ResponseEntity.ok(GenericResponse.success(model, i18n.translate("controller.workout.model.delete.successful")))).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @Operation(summary = "Get all workout model", description = "Retrieves all workout model.")
  @GetMapping()
  public ResponseEntity<GenericResponse<ArrayList<WorkoutModel>>> getAllWorkoutModel(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(GenericResponse.success(workoutModelService.findAll(userDetails.getId()), i18n.translate("controller.workout.model.return-all.successful")));
  }

  @Operation(summary = "Update workout model", description = "Patch workout model with ID.")
  @PatchMapping("/{id}")
  public ResponseEntity<GenericResponse<WorkoutModel>> updateWorkoutModel(@PathVariable("id") Long id, @RequestBody WorkoutModelCreateDTO workoutModelCreateDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Optional<WorkoutModel> workoutModel = workoutModelService.update(id, workoutModelCreateDTO, userDetails.getId());

    return workoutModel.map(model -> ResponseEntity.ok(GenericResponse.success(model, i18n.translate("controller.workout.model.update.successful")))).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @Operation(summary = "Get all workout model exercises", description = "Retrieves all workout model exercises.")
  @GetMapping("/{id}/exercises")
  public ResponseEntity<GenericResponse<ArrayList<WorkoutExercise>>> getAllWorkoutModelExercises(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(GenericResponse.success(workoutModelService.findAllExercises(id, userDetails.getId()), i18n.translate("controller.workout.model.return-all-exercises.successful")));
  }

  @Operation(summary = "Add schedule to workout model", description = "Add a schedule to a workout model.")
  @PostMapping("/{workoutModelId}/schedule")
  public ResponseEntity<GenericResponse<Schedule>> addScheduleToWorkoutModel(
      @PathVariable("workoutModelId") Long workoutModelId,
      @RequestBody ScheduleCreateDTO scheduleCreateDTO,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    Schedule createdSchedule = this.workoutModelService.addSchedule(workoutModelId, scheduleCreateDTO, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(createdSchedule, i18n.translate("controller.workout.model.add-schedule.successful")));
  }
  
  @Operation(summary = "Remove schedule from workout model", description = "Remove a schedule from a workout model.")
  @DeleteMapping("/schedule/{scheduleId}")
  public ResponseEntity<GenericResponse<Schedule>> removeScheduleFromWorkoutModel(
      @PathVariable("scheduleId") Long scheduleId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    this.workoutModelService.removeSchedule(scheduleId, userDetails.getId());
    return ResponseEntity.ok(GenericResponse.success(null, i18n.translate("controller.workout.model.remove-schedule.successful")));
  }

  // todo update schedule, update exercise, remove exercise, remove schedule

}
