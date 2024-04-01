package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.model.dto.WorkoutModelSessionDateDTO;
import com.blinnproject.myworkdayback.model.response.GenericResponse;
import com.blinnproject.myworkdayback.security.UserDetailsImpl;
import com.blinnproject.myworkdayback.service.i18n.I18nService;
import com.blinnproject.myworkdayback.service.workout_model.WorkoutModelService;
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

@Tag(name="Calendar", description = "Endpoints related to user calendar.")
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/api/calendar", consumes="application/json", produces="application/json")
public class CalendarController {

  private final I18nService i18n;

  private final WorkoutModelService workoutModelService;

  public CalendarController(I18nService i18n, WorkoutModelService workoutModelService) {
    this.i18n = i18n;
    this.workoutModelService = workoutModelService;
  }

  @Operation(summary = "Get workout model plan session list by date", description = "Retrieves all planned workout model sessions by date.")
  @GetMapping("/workout-model/{startDate}/{endDate}")
  public ResponseEntity<GenericResponse<List<WorkoutModelSessionDateDTO>>> getAllWorkoutModelPlanSessionsByDate(
      @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
      @PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    List<WorkoutModelSessionDateDTO> workoutModelSessionsDate = workoutModelService.getAllWorkoutModelPlanSessionsByDate(startDate, endDate, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(workoutModelSessionsDate, i18n.translate("controller.exercise.return-all.successful")));
  }

}
