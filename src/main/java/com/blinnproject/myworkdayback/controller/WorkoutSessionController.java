package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.model.entity.WorkoutSession;
import com.blinnproject.myworkdayback.model.response.GenericResponse;
import com.blinnproject.myworkdayback.security.UserDetailsImpl;
import com.blinnproject.myworkdayback.service.csv.CSVService;
import com.blinnproject.myworkdayback.service.i18n.I18nService;
import com.blinnproject.myworkdayback.service.workout_session.WorkoutSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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

  @Operation(summary = "Create workout session", description = "Creates a workout session by workout model and date.")
  @PostMapping(value = "/{startedAt}/workout-model/{workoutModelId}")
  public ResponseEntity<GenericResponse<WorkoutSession>> createWorkoutSession(
      @PathVariable("startedAt") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startedAt,
      @PathVariable("workoutModelId") Long workoutModelId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    WorkoutSession workoutSession = workoutSessionService.createWorkoutSession(startedAt, workoutModelId, userDetails.getId());

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(GenericResponse.success(workoutSession, i18n.translate("controller.workout.session.create.successful")));
  }

  @Operation(summary = "Get workout session details", description = "")
  @GetMapping(value = "/{id}")
  public ResponseEntity<GenericResponse<WorkoutSession>> getWorkoutSessionDetails(
    @PathVariable("id") Long id,
    @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    WorkoutSession workoutSession = workoutSessionService.find(id, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(workoutSession, i18n.translate("controller.workout.session.return-details.successful")));
  }

  // todo add workout_exercise/set ?? by session id

  // todo update workout_exercise/set ?? by session id

  // todo remove workout_exercise/set ?? by session id

  // todo set manually session data (with data change with model)

  // todo check manually session (without data change with model)
}
