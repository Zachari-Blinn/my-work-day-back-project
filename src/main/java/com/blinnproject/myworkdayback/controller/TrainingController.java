package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.model.Training;
import com.blinnproject.myworkdayback.model.TrainingExercises;
import com.blinnproject.myworkdayback.payload.request.AddExerciseRequest;
import com.blinnproject.myworkdayback.payload.request.CreateTrainingRequest;
import com.blinnproject.myworkdayback.payload.request.ModifyBeforeValidateRequest;
import com.blinnproject.myworkdayback.payload.request.ValidateTrainingRequest;
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
import java.util.Map;
import java.util.Optional;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/training")
public class TrainingController {

  @Autowired
  TrainingService trainingService;

  @PostMapping()
  public ResponseEntity<GenericResponse<Training>> create(@Valid @RequestBody CreateTrainingRequest trainingRequest) {
    try {
      Training _training = trainingService.create(trainingRequest);
      return ResponseEntity.status(HttpStatus.CREATED).body(GenericResponse.success(_training, "Training was successfully created!"));
    } catch (Exception exception) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/current-user")
  public ResponseEntity<GenericResponse<List<Training>>> getCurrentUserTrainings() {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long createdBy = userDetails.getId();
    try {
      List<Training> trainings = trainingService.getAllTrainingsByCreatedBy(createdBy);

      if (trainings.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return ResponseEntity.ok(GenericResponse.success(trainings, "Return all trainings of current user successfully!"));
    } catch (Exception exception) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<GenericResponse<Training>> getTrainingById(@PathVariable("id") Long id) {
    try {
      Optional<Training> trainingData = trainingService.findById(id);

      return trainingData.map(training -> ResponseEntity.ok(GenericResponse.success(training, "Return training by id successfully!"))).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/{trainingId}/add-exercise")
  public ResponseEntity<GenericResponse<?>> addExercise(@PathVariable("trainingId") Long trainingId, @Valid @RequestBody AddExerciseRequest requestBody) {
    try {
      TrainingExercises createdTrainingExercises = this.trainingService.addExercise(trainingId, requestBody);

      return ResponseEntity.ok(GenericResponse.success(createdTrainingExercises, "Exercise added to training successfully!"));
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{trainingId}/exercises")
  public ResponseEntity<GenericResponse<List<TrainingExercises>>> getExercisesByTrainingId(
      @RequestParam(defaultValue = "false") Boolean fetchTemplate,
      @PathVariable("trainingId") Long trainingId
  ) {
    try {
      List<TrainingExercises> trainingExercises;

      if (fetchTemplate) {
        trainingExercises = this.trainingService.getTemplateExercisesByTrainingId(trainingId);
      } else {
        trainingExercises = this.trainingService.getExercisesByTrainingId(trainingId);
      }

      return ResponseEntity.ok(GenericResponse.success(trainingExercises, "Return all exercises by training successfully!"));
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/{trainingId}/validate/{trainingDay}")
  public ResponseEntity<GenericResponse<List<TrainingExercises>>> validateTraining(
      @PathVariable("trainingId") Long trainingId,
      @PathVariable("trainingDay") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDay
  ) {
    try {
      List<TrainingExercises> createdTrainingExercises = this.trainingService.validateTrainingExercises(trainingId, trainingDay);

      return ResponseEntity.ok(GenericResponse.success(createdTrainingExercises, "Validate training session successfully!"));
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/{trainingId}/modify-before-validate")
  public ResponseEntity<List<TrainingExercises>> modifyBeforeValidate(@PathVariable("trainingId") Long trainingId, @RequestBody ModifyBeforeValidateRequest requestBody) {
    try {
      List<TrainingExercises> createdTrainingExercises = this.trainingService.modifyBeforeValidate(trainingId, requestBody);

      return new ResponseEntity<>(createdTrainingExercises, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{trainingId}/validate-training-day/{trainingDate}")
  public ResponseEntity<GenericResponse<List<FormattedTrainingData>>> checkIfTrainingExercisesSeriesIsCompleted(
      @PathVariable("trainingId") Long trainingId,
      @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDate
  ) {
    try {
      List<TrainingExercisesSeriesInfo> trainingExercisesSeriesInfoList = this.trainingService.getTrainingSeriesStatusByDate(trainingId, trainingDate);

      List<FormattedTrainingData> transformedData = this.trainingService.formatTrainingExercisesSeriesInfo(trainingExercisesSeriesInfoList);

      return ResponseEntity.ok(GenericResponse.success(transformedData, "Return training session info of selected day successfully!"));
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/validate-training-day/{trainingDate}")
  public ResponseEntity<GenericResponse<List<FormattedTrainingData>>> checkIfTrainingExercisesSeriesIsCompleted(
      @PathVariable("trainingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date trainingDate
  ) {
    try {
      List<TrainingExercisesSeriesInfo> trainingExercisesSeriesInfoList = this.trainingService.getAllTrainingsSeriesStatusByDate(trainingDate);

      List<FormattedTrainingData> transformedData = this.trainingService.formatTrainingExercisesSeriesInfo(trainingExercisesSeriesInfoList);

      return ResponseEntity.ok(GenericResponse.success(transformedData, "Return training session info of selected day successfully!"));
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
