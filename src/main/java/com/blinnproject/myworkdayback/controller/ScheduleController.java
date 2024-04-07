package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.model.projection.CombinedWorkoutInfoDTO;
import com.blinnproject.myworkdayback.model.projection.WorkoutScheduleDTO;
import com.blinnproject.myworkdayback.model.response.GenericResponse;
import com.blinnproject.myworkdayback.security.UserDetailsImpl;
import com.blinnproject.myworkdayback.service.i18n.I18nService;
import com.blinnproject.myworkdayback.service.schedule.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name="Schedule", description = "Endpoints related to the user's workout schedule")
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/api/schedule", produces="application/json")
public class ScheduleController {

  private final I18nService i18n;

  private final ScheduleService scheduleService;

  public ScheduleController(I18nService i18n, ScheduleService scheduleService) {
    this.i18n = i18n;
    this.scheduleService = scheduleService;
  }

  @Operation(summary = "Retrieve scheduled workout for a given period", description = "This function allows retrieving the workout schedule for a user over a specified period.")
  @GetMapping("/workout/{startDate}/{endDate}")
  public ResponseEntity<GenericResponse<List<WorkoutScheduleDTO>>> getScheduledWorkoutForPeriod(
      @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
      @PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    List<WorkoutScheduleDTO> workoutModelSessionsDate = scheduleService.getWorkoutScheduleForPeriod(startDate, endDate, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(workoutModelSessionsDate, i18n.translate("controller.exercise.return-all.successful")));
  }

  @Operation(summary = "List program of completed and upcoming sessions for a given period", description = "This function lists the program of completed and upcoming sessions for a user over a specified period.")
  @GetMapping("/workout-session/{startDate}/{endDate}")
  public ResponseEntity<GenericResponse<List<CombinedWorkoutInfoDTO>>> getWorkoutSessionsForPeriod(
      @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
      @PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    List<CombinedWorkoutInfoDTO> workoutModelSessionsDate = scheduleService.getWorkoutSessionsForPeriod(startDate, endDate, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(workoutModelSessionsDate, i18n.translate("controller.exercise.return-all.successful")));
  }

  // todo get details of a specific workout session

}
