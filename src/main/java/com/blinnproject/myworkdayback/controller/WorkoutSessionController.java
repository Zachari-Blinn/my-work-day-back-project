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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Tag(name="Workout Session", description = "Endpoints related to user workout sessions.")
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/workout-session")
public class WorkoutSessionController {
  private final I18nService i18n;
  private final WorkoutSessionService workoutSessionService;
  private final CSVService csvService;

  public WorkoutSessionController(I18nService i18n, WorkoutSessionService workoutSessionService, CSVService csvService) {
    this.i18n = i18n;
    this.workoutSessionService = workoutSessionService;
    this.csvService = csvService;
  }

  @Operation(summary = "Get workout session by date", description = "Retrieves a workout session by its date.")
  @GetMapping("/{trainingDate}")
  public ResponseEntity<GenericResponse<List<WorkoutSession>>> getAllWorkoutSessionsByDate(
      @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate trainingDate,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    List<WorkoutSession> workoutSessions = workoutSessionService.findAllByDate(trainingDate, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(workoutSessions, i18n.translate("controller.workout.session.return-by-date.successful")));
  }

  // todo start session

  // todo update session

  // todo set manually session data (with data change with model)

  // todo check manually session (without data change with model)
}
