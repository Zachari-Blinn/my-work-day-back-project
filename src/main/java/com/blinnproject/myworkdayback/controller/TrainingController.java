package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.model.Training;
import com.blinnproject.myworkdayback.model.TrainingExercises;
import com.blinnproject.myworkdayback.payload.dto.training.TrainingCreateDTO;
import com.blinnproject.myworkdayback.payload.dto.training_exercises.TrainingExercisesCreateDTO;
import com.blinnproject.myworkdayback.payload.query.TrainingCalendarDTO;
import com.blinnproject.myworkdayback.payload.request.ModifyBeforeValidateRequest;
import com.blinnproject.myworkdayback.payload.response.FormattedTrainingData;
import com.blinnproject.myworkdayback.payload.response.GenericResponse;
import com.blinnproject.myworkdayback.payload.response.TrainingExercisesSeriesInfo;
import com.blinnproject.myworkdayback.security.UserDetailsImpl;
import com.blinnproject.myworkdayback.service.training.TrainingService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/training")
public class TrainingController {

  private final TrainingService trainingService;

  public TrainingController(TrainingService trainingService) {
    this.trainingService = trainingService;
  }

  @PostMapping()
  public ResponseEntity<GenericResponse<Training>> create(@Valid @RequestBody TrainingCreateDTO trainingDTO) {
    Training training = trainingService.create(trainingDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(GenericResponse.success(training, "Training was successfully created!"));
  }

  @GetMapping("/current-user")
  public ResponseEntity<GenericResponse<List<Training>>> getCurrentUserTrainings() {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    List<Training> trainings = trainingService.getAllTrainingsByCreatedBy(userDetails.getId());

    if (trainings.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return ResponseEntity.ok(GenericResponse.success(trainings, "Return all trainings of current user successfully!"));
  }

  @GetMapping("/{id}")
  public ResponseEntity<GenericResponse<Training>> getTrainingById(@PathVariable("id") Long id) {
    Optional<Training> trainingData = trainingService.findById(id);

    return trainingData.map(training -> ResponseEntity.ok(GenericResponse.success(training, "Return training by id successfully!"))).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping("/{trainingId}/exercise/{exerciseId}")
  public ResponseEntity<GenericResponse<TrainingExercises>> addExercise(
      @PathVariable("trainingId") Long trainingId,
      @PathVariable("exerciseId") Long exerciseId,
      @Valid @RequestBody TrainingExercisesCreateDTO trainingExercisesDTO
  ) {
    TrainingExercises createdTrainingExercises = this.trainingService.addExercise(trainingId, exerciseId, trainingExercisesDTO);

    return ResponseEntity.ok(GenericResponse.success(createdTrainingExercises, "Exercise added to training successfully!"));
  }

  @GetMapping("/{trainingId}/exercises")
  public ResponseEntity<GenericResponse<List<TrainingExercises>>> getExercisesByTrainingId(
      @RequestParam(defaultValue = "false") Boolean fetchTemplate,
      @PathVariable("trainingId") Long trainingId
  ) {
    List<TrainingExercises> trainingExercises;

    if (fetchTemplate) {
      trainingExercises = this.trainingService.getTemplateExercisesByTrainingId(trainingId);
    } else {
      trainingExercises = this.trainingService.getExercisesByTrainingId(trainingId);
    }

    return ResponseEntity.ok(GenericResponse.success(trainingExercises, "Return all exercises by training successfully!"));
  }

  @PostMapping("/{trainingParentId}/validate/{trainingDate}")
  public ResponseEntity<GenericResponse<List<TrainingExercises>>> validateTraining(
      @PathVariable("trainingParentId") Long trainingParentId,
      @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDate
  ) {
    List<TrainingExercises> createdTrainingExercises = this.trainingService.validateTraining(trainingParentId, trainingDate);

    return ResponseEntity.ok(GenericResponse.success(createdTrainingExercises, "Validate training session successfully!"));
  }

  @PostMapping("/{trainingParentId}/modify-and-validate/{trainingDate}")
  public ResponseEntity<GenericResponse<List<TrainingExercises>>> modifyAndValidate(
      @PathVariable("trainingParentId") Long trainingParentId,
      @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDate,
      @RequestBody ModifyBeforeValidateRequest requestBody
  ) {
    List<TrainingExercises> createdTrainingExercises = this.trainingService.modifyAndValidateTraining(trainingParentId, trainingDate, requestBody);

    return ResponseEntity.ok(GenericResponse.success(createdTrainingExercises, "Modify and validate training session successfully!"));
  }

  @GetMapping("/{trainingId}/validate-training-day/{trainingDate}")
  public ResponseEntity<GenericResponse<List<FormattedTrainingData>>> returnTrainingSessionInfo(
      @PathVariable("trainingId") Long trainingId,
      @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDate
  ) {
    List<TrainingExercisesSeriesInfo> trainingExercisesSeriesInfoList = this.trainingService.getTrainingSeriesStatusByDate(trainingId, trainingDate);

    List<FormattedTrainingData> transformedData = this.trainingService.formatTrainingExercisesSeriesInfo(trainingExercisesSeriesInfoList, trainingDate);

    return ResponseEntity.ok(GenericResponse.success(transformedData, "Return training session info of selected day successfully!"));
  }

  @GetMapping("/validate-training-day/{trainingDate}")
  public ResponseEntity<GenericResponse<List<FormattedTrainingData>>> returnAllTrainingSessionInfo(
      @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDate
  ) {
    List<TrainingExercisesSeriesInfo> trainingExercisesSeriesInfoList = this.trainingService.getAllTrainingsSeriesStatusByDate(trainingDate);

    List<FormattedTrainingData> transformedData = this.trainingService.formatTrainingExercisesSeriesInfo(trainingExercisesSeriesInfoList, trainingDate);

    return ResponseEntity.ok(GenericResponse.success(transformedData, "Return training session info of selected day successfully!"));
  }

  @DeleteMapping("/{trainingParentId}/reset-training-session-day/{trainingDate}")
  public ResponseEntity<GenericResponse<?>> resetTrainingDay(
      @PathVariable("trainingParentId") Long trainingParentId,
      @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDate
  ) {
    this.trainingService.resetTrainingDay(trainingParentId, trainingDate);

    return ResponseEntity.ok(GenericResponse.success(null, "Cancelled training session info of selected day successfully!"));
  }

  @DeleteMapping("/{trainingParentId}/cancel-training-session-day/{trainingDate}")
  public ResponseEntity<GenericResponse<?>> cancelTrainingDay(
      @PathVariable("trainingParentId") Long trainingParentId,
      @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDate
  ) {
    this.trainingService.cancelTrainingDay(trainingParentId, trainingDate);

    return ResponseEntity.ok(GenericResponse.success(null, "Cancelled training session info of selected day successfully!"));
  }

  @GetMapping("/calendar/{startDate}/{endDate}")
  public ResponseEntity<GenericResponse<List<TrainingCalendarDTO>>> returnCalendarInfo(
      @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
      @PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
  ) throws Exception {
    List<TrainingCalendarDTO> result = this.trainingService.getTrainingCalendarInfo(startDate, endDate);

    return ResponseEntity.ok(GenericResponse.success(result, "Return calendar info successfully!"));
  }
}
