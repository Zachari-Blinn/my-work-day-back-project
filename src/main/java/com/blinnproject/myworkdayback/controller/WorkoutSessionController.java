package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.model.entity.WorkoutSession;
import com.blinnproject.myworkdayback.model.entity.WorkoutSet;
import com.blinnproject.myworkdayback.model.response.GenericResponse;
import com.blinnproject.myworkdayback.security.UserDetailsImpl;
import com.blinnproject.myworkdayback.service.csv.CSVService;
import com.blinnproject.myworkdayback.service.i18n.I18nService;
import com.blinnproject.myworkdayback.service.workout_session.WorkoutSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name="Workout Session", description = "Endpoints related to user workout sessions.")
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/api/workout-session", produces="application/json")
public class WorkoutSessionController {
  private final I18nService i18n;
  private final WorkoutSessionService workoutSessionService;
  private final CSVService csvService;

  public WorkoutSessionController(I18nService i18n, WorkoutSessionService workoutSessionService, CSVService csvService) {
    this.i18n = i18n;
    this.workoutSessionService = workoutSessionService;
    this.csvService = csvService;
  }

  @Operation(summary = "Start workout session", description = "Start a workout session by workout model and date.")
  @PostMapping(value = "/{startedAt}/workout-model/{workoutModelId}")
  public ResponseEntity<GenericResponse<WorkoutSession>> startWorkoutSession(
      @PathVariable("startedAt") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startedAt,
      @PathVariable("workoutModelId") Long workoutModelId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    // peut être saisir directement la date depuis le service
    WorkoutSession workoutSession = workoutSessionService.createWorkoutSession(startedAt, workoutModelId, userDetails.getId());

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(GenericResponse.success(workoutSession, i18n.translate("controller.workout.session.create.successful")));
  }

  @Operation(summary = "Get workout session details", description = "Get workout session details by id.")
  @GetMapping(value = "/{id}")
  public ResponseEntity<GenericResponse<WorkoutSession>> getWorkoutSessionDetails(
    @PathVariable("id") Long id,
    @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    WorkoutSession workoutSession = workoutSessionService.find(id, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(workoutSession, i18n.translate("controller.workout.session.return-details.successful")));
  }

  @Operation(summary = "Update set of workout session", description = "Updates a set of workout session by id.")
  @PutMapping(value = "/workout-set/{workoutSetId}")
  public ResponseEntity<GenericResponse<WorkoutSession>> updateWorkoutSessionSet(
    @PathVariable("workoutSetId") Long workoutSetId,
    @Valid @RequestBody WorkoutSet workoutSet,
    @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    WorkoutSession workoutSession = workoutSessionService.updateWorkoutSessionSet(workoutSetId, workoutSet, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(workoutSession, i18n.translate("controller.workout.session.update-set.successful")));
  }

  // todo route : approve workout exercise sets by workout_exercise_id
  // todo route : reset approval of workout exercise sets by workout_exercise_id

  @Operation(summary = "Create manually workout session by workout model", description = "Create a workout session manually by workout model.")
  @PostMapping(value = "/{startedAt}/{endedAt}/workout-model/{workoutModelId}")
  public ResponseEntity<GenericResponse<WorkoutSession>> createWorkoutSession(
    @PathVariable("startedAt") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startedAt,
    @PathVariable("endedAt") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endedAt,
    @PathVariable("workoutModelId") Long workoutModelId,
    @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    WorkoutSession workoutSessionIn = workoutSessionService.createWorkoutSessionManually(startedAt, endedAt, workoutModelId, userDetails.getId());

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(GenericResponse.success(workoutSessionIn, i18n.translate("controller.workout.session.create.successful")));
  }

  // todo route : finish workout session by id or update workout session

  // todo route : delete session by id
}
