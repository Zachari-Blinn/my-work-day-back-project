package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.model.Training;
import com.blinnproject.myworkdayback.model.TrainingExercises;
import com.blinnproject.myworkdayback.payload.dto.training.TrainingCreateDTO;
import com.blinnproject.myworkdayback.payload.dto.training_exercises.TrainingExercisesCreateDTO;
import com.blinnproject.myworkdayback.payload.query.TrainingCalendarDTO;
import com.blinnproject.myworkdayback.payload.request.ModifyAndValidateRequest;
import com.blinnproject.myworkdayback.payload.response.FormattedTrainingData;
import com.blinnproject.myworkdayback.payload.response.GenericResponse;
import com.blinnproject.myworkdayback.payload.response.TrainingExercisesSeriesInfo;
import com.blinnproject.myworkdayback.security.UserDetailsImpl;
import com.blinnproject.myworkdayback.service.i18n.I18nService;
import com.blinnproject.myworkdayback.service.training.TrainingService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/training")
public class TrainingController {

  private final I18nService i18n;
  private final TrainingService trainingService;

  public TrainingController(I18nService i18nService, TrainingService trainingService) {
    this.i18n = i18nService;
    this.trainingService = trainingService;
  }

  @PostMapping()
  public ResponseEntity<GenericResponse<Training>> create(@Valid @RequestBody TrainingCreateDTO trainingDTO) {
    Training training = trainingService.create(trainingDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(GenericResponse.success(training, i18n.getRequestLocalizedMessage("controller.training.create.successful")));
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.0", forRemoval = false)
  @GetMapping("/current-user")
  public ResponseEntity<GenericResponse<List<Training>>> getCurrentUserTrainings() {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    List<Training> trainings = trainingService.getAllTrainingsByCreatedBy(userDetails.getId());

    if (trainings.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return ResponseEntity.ok(GenericResponse.success(trainings, i18n.getRequestLocalizedMessage("controller.training.return-all-trainings.successful")));
  }

  @GetMapping("/{id}")
  public ResponseEntity<GenericResponse<Training>> getTrainingById(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Optional<Training> trainingData = trainingService.findById(id, userDetails.getId());

    return trainingData.map(training -> ResponseEntity.ok(GenericResponse.success(training, i18n.getRequestLocalizedMessage("controller.training.return-training-by-id.successful")))).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping("/{trainingId}/exercise/{exerciseId}")
  public ResponseEntity<GenericResponse<TrainingExercises>> addExerciseToTraining(
      @PathVariable("trainingId") Long trainingId,
      @PathVariable("exerciseId") Long exerciseId,
      @Valid @RequestBody TrainingExercisesCreateDTO trainingExercisesDTO,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    TrainingExercises createdTrainingExercises = this.trainingService.addExercise(trainingId, exerciseId, trainingExercisesDTO, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(createdTrainingExercises, i18n.getRequestLocalizedMessage("controller.training.add-exercise.successful")));
  }

  @Deprecated
  @GetMapping("/{trainingId}/exercises")
  public ResponseEntity<GenericResponse<List<TrainingExercises>>> getExercisesByTrainingId(
      @RequestParam(defaultValue = "false") Boolean fetchTemplate,
      @PathVariable("trainingId") Long trainingId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    List<TrainingExercises> trainingExercises;

    if (Boolean.TRUE.equals(fetchTemplate)) {
      trainingExercises = this.trainingService.getTemplateExercisesByTrainingId(trainingId, userDetails.getId());
    } else {
      trainingExercises = this.trainingService.getExercisesByTrainingId(trainingId, userDetails.getId());
    }

    return ResponseEntity.ok(GenericResponse.success(trainingExercises, "Return all exercises by training successfully!"));
  }

  @PostMapping("/{trainingParentId}/validate/{trainingDate}")
  public ResponseEntity<GenericResponse<List<TrainingExercises>>> validateTrainingSession(
      @PathVariable("trainingParentId") Long trainingParentId,
      @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDate,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    List<TrainingExercises> createdTrainingExercises = this.trainingService.validateTraining(trainingParentId, trainingDate, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(createdTrainingExercises, i18n.getRequestLocalizedMessage("controller.training.validate.successful")));
  }

  @PostMapping("/{trainingParentId}/modify-and-validate/{trainingDate}")
  public ResponseEntity<GenericResponse<List<TrainingExercises>>> modifyAndValidateTrainingSession(
      @PathVariable("trainingParentId") Long trainingParentId,
      @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDate,
      @RequestBody ModifyAndValidateRequest requestBody,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    List<TrainingExercises> createdTrainingExercises = this.trainingService.modifyAndValidateTraining(trainingParentId, trainingDate, requestBody, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(createdTrainingExercises, i18n.getRequestLocalizedMessage("controller.training.modify-and-validate.successful")));
  }

  @Deprecated
  @GetMapping("/{trainingId}/validate/{trainingDate}")
  public ResponseEntity<GenericResponse<List<FormattedTrainingData>>> returnTrainingSessionInfo(
      @PathVariable("trainingId") Long trainingId,
      @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDate,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    List<TrainingExercisesSeriesInfo> trainingExercisesSeriesInfoList = this.trainingService.getTrainingSeriesStatusByDate(trainingId, trainingDate, userDetails.getId());

    List<FormattedTrainingData> transformedData = this.trainingService.formatTrainingExercisesSeriesInfo(trainingExercisesSeriesInfoList, trainingDate);

    return ResponseEntity.ok(GenericResponse.success(transformedData, "Return training session info of selected day successfully!"));
  }

  @GetMapping("/validate/{trainingDate}")
  public ResponseEntity<GenericResponse<List<FormattedTrainingData>>> returnAllTrainingSessionInfo(
      @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDate,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    List<TrainingExercisesSeriesInfo> trainingExercisesSeriesInfoList = this.trainingService.getAllTrainingsSeriesStatusByDate(trainingDate, userDetails.getId());

    List<FormattedTrainingData> transformedData = this.trainingService.formatTrainingExercisesSeriesInfo(trainingExercisesSeriesInfoList, trainingDate);

    return ResponseEntity.ok(GenericResponse.success(transformedData, i18n.getRequestLocalizedMessage("controller.training.return-all-trainings-info.successful")));
  }

  @DeleteMapping("/{trainingParentId}/reset/{trainingDate}")
  public ResponseEntity<GenericResponse<?>> resetTrainingSession(
      @PathVariable("trainingParentId") Long trainingParentId,
      @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDate,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    this.trainingService.resetTrainingDay(trainingParentId, trainingDate, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(null, i18n.getRequestLocalizedMessage("controller.training.reset.successful")));
  }

  @DeleteMapping("/{trainingParentId}/cancel/{trainingDate}")
  public ResponseEntity<GenericResponse<?>> cancelTrainingSession(
      @PathVariable("trainingParentId") Long trainingParentId,
      @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDate,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    this.trainingService.cancelTrainingDay(trainingParentId, trainingDate, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(null, i18n.getRequestLocalizedMessage("controller.training.cancel.successful")));
  }

  @GetMapping("/calendar/{startDate}/{endDate}")
  public ResponseEntity<GenericResponse<List<TrainingCalendarDTO>>> returnTrainingCalendarInfo(
      @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
      @PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) throws Exception {
    List<TrainingCalendarDTO> result = this.trainingService.getTrainingCalendarInfo(startDate, endDate, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(result, i18n.getRequestLocalizedMessage("controller.training.return-calendar-info.successful")));
  }
}
