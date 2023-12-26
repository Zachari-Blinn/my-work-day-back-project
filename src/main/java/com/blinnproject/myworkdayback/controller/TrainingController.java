package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.model.Training;
import com.blinnproject.myworkdayback.model.TrainingExercises;
import com.blinnproject.myworkdayback.payload.request.AddExerciseRequest;
import com.blinnproject.myworkdayback.payload.request.CreateTrainingRequest;
import com.blinnproject.myworkdayback.payload.request.ModifyBeforeValidateRequest;
import com.blinnproject.myworkdayback.payload.response.FormattedTrainingData;
import com.blinnproject.myworkdayback.payload.response.GenericResponse;
import com.blinnproject.myworkdayback.payload.response.TrainingExercisesSeriesInfo;
import com.blinnproject.myworkdayback.security.UserDetailsImpl;
import com.blinnproject.myworkdayback.service.training.TrainingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/training")
public class TrainingController {

  private TrainingService trainingService;

  public TrainingController(TrainingService trainingService) {
    this.trainingService = trainingService;
  }

  @PostMapping()
  public ResponseEntity<GenericResponse<Training>> create(@Valid @RequestBody CreateTrainingRequest trainingRequest) {
    Training training = trainingService.create(trainingRequest);

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

  @PostMapping("/{trainingId}/add-exercise")
  public ResponseEntity<GenericResponse<TrainingExercises>> addExercise(@PathVariable("trainingId") Long trainingId, @Valid @RequestBody AddExerciseRequest requestBody) {
    TrainingExercises createdTrainingExercises = this.trainingService.addExercise(trainingId, requestBody);

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

  @PostMapping("/{trainingId}/validate/{trainingDate}")
  public ResponseEntity<GenericResponse<List<TrainingExercises>>> validateTraining(
      @PathVariable("trainingId") Long trainingId,
      @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDate
  ) {
    List<TrainingExercises> createdTrainingExercises = this.trainingService.validateTrainingExercises(trainingId, trainingDate);

    return ResponseEntity.ok(GenericResponse.success(createdTrainingExercises, "Validate training session successfully!"));
  }

  @PostMapping("/{trainingId}/modify-before-validate/{trainingDate}")
  public ResponseEntity<GenericResponse<List<TrainingExercises>>> modifyAndValidate(
      @PathVariable("trainingId") Long trainingId,
      @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDate,
      @RequestBody ModifyBeforeValidateRequest requestBody
  ) {
    List<TrainingExercises> createdTrainingExercises = this.trainingService.modifyBeforeValidate(trainingId, trainingDate, requestBody);

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
}
