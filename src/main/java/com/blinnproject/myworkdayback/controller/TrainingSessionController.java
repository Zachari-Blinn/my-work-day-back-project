package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.model.entity.TrainingExercises;
import com.blinnproject.myworkdayback.payload.request.ModifyAndValidateRequest;
import com.blinnproject.myworkdayback.payload.response.FormattedTrainingData;
import com.blinnproject.myworkdayback.payload.response.GenericResponse;
import com.blinnproject.myworkdayback.security.UserDetailsImpl;
import com.blinnproject.myworkdayback.service.csv.CSVService;
import com.blinnproject.myworkdayback.service.i18n.I18nService;
import com.blinnproject.myworkdayback.service.training_session.TrainingSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Tag(name="Training Session", description = "Endpoints related to user training sessions.")
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/training-session")
public class TrainingSessionController {
  private final I18nService i18n;
  private final TrainingSessionService trainingSessionService;
  private final CSVService csvService;

  public TrainingSessionController(I18nService i18n, TrainingSessionService trainingSessionService, CSVService csvService) {
    this.i18n = i18n;
    this.trainingSessionService = trainingSessionService;
    this.csvService = csvService;
  }

  @Operation(summary = "Return All Training Session Info", description = "Retrieves information about all training sessions on a specified date.")
  @GetMapping("/{trainingDate}")
  public ResponseEntity<GenericResponse<List<FormattedTrainingData>>> returnAllTrainingSessionInfo(
    @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDate,
    @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    List<FormattedTrainingData> transformedData = this.trainingSessionService.getAllTrainingsSeriesStatusByDate(trainingDate, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(transformedData, i18n.translate("controller.training.return-all-trainings-info.successful")));
  }

  @Operation(summary = "Validate Training Session without Change", description = "Validates the training session without modifying the associated template data.")
  @PostMapping("/{trainingParentId}/validate/{trainingDate}")
  public ResponseEntity<GenericResponse<List<TrainingExercises>>> validateTrainingSessionWithoutChange(
    @PathVariable("trainingParentId") Long trainingParentId,
    @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDate,
    @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    List<TrainingExercises> trainingExercises = this.trainingSessionService.validateTrainingSessionWithoutChange(trainingParentId, trainingDate, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(trainingExercises, i18n.translate("controller.training.validate.successful")));
  }

  @Operation(summary = "Validate Training Session with Change", description = "Validates the training session with modifying the associated template data.")
  @PostMapping("/{trainingParentId}/modify-and-validate/{trainingDate}")
  public ResponseEntity<GenericResponse<List<TrainingExercises>>> modifyDataAndValidateTrainingSession(
    @PathVariable("trainingParentId") Long trainingParentId,
    @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDate,
    @RequestBody ModifyAndValidateRequest requestBody,
    @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    List<TrainingExercises> trainingExercises = this.trainingSessionService.modifyDataAndValidateTrainingSession(trainingParentId, trainingDate, requestBody, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(trainingExercises, i18n.translate("controller.training.modify-and-validate.successful")));
  }

  @Operation(summary = "Reset Training Session of the Day", description = "Resets the training session of the specified day.")
  @DeleteMapping("/{trainingParentId}/reset/{trainingDate}")
  public ResponseEntity<GenericResponse<Void>> resetTrainingSessionOfTheDay(
    @PathVariable("trainingParentId") Long trainingParentId,
    @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDate,
    @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    this.trainingSessionService.resetTrainingSessionOfTheDay(trainingParentId, trainingDate, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(null, i18n.translate("controller.training.reset.successful")));
  }

  @Operation(summary = "Cancel Training Session of the Day", description = "Cancels the training session of the specified day.")
  @DeleteMapping("/{trainingParentId}/cancel/{trainingDate}")
  public ResponseEntity<GenericResponse<Void>> cancelTrainingSessionOfTheDay(
    @PathVariable("trainingParentId") Long trainingParentId,
    @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDate,
    @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    this.trainingSessionService.cancelTrainingSessionOfTheDay(trainingParentId, trainingDate, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(null, i18n.translate("controller.training.cancel.successful")));
  }
  @Operation(summary = "Return All Training Sessions Info CSV", description = "Returns all training sessions information in CSV format.")
  @GetMapping("/csv/{startDate}/{endDate}")
  public ResponseEntity<Resource> returnAllTrainingSessionsInfoCSV(
    @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
    @PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
    @AuthenticationPrincipal UserDetailsImpl userDetails
  ) throws IOException {
    String filename = userDetails.getUsername() + "_training-sessions.csv";
    InputStreamResource file = new InputStreamResource(csvService.trainingSessionToCsv(userDetails.getId(), startDate, endDate));

    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
      .contentType(MediaType.parseMediaType("application/csv"))
      .body(file);
  }
}
