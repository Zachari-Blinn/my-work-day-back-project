package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.model.*;
import com.blinnproject.myworkdayback.payload.request.AddExerciseRequest;
import com.blinnproject.myworkdayback.payload.request.ValidateTrainingRequest;
import com.blinnproject.myworkdayback.service.training.TrainingService;
import com.blinnproject.myworkdayback.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  TrainingService trainingService;

  @PostMapping()
  public ResponseEntity<Training> create(@Valid @RequestBody Training trainingRequest) {
    try {
      Training _training = trainingService.create(trainingRequest);
      return new ResponseEntity<>(_training, HttpStatus.CREATED);
    } catch (Exception exception) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/current-user")
  public ResponseEntity<List<Training>> getCurrentUserTrainings() {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long createdBy = userDetails.getId();
    try {
      List<Training> trainings = trainingService.getAllTrainingsByCreatedBy(createdBy);

      if (trainings.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(trainings, HttpStatus.OK);
    } catch (Exception exception) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Training> getTrainingById(@PathVariable("id") Long id) {
    try {
      Optional<Training> trainingData = trainingService.findById(id);

      return trainingData.map(training -> new ResponseEntity<>(training, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/{trainingId}/add-exercise")
  public ResponseEntity<?> addExercise(@PathVariable("trainingId") Long trainingId, @Valid @RequestBody AddExerciseRequest requestBody) {
    try {
      TrainingExercises createdTrainingExercises = this.trainingService.addExercise(trainingId, requestBody);

      return new ResponseEntity<>(createdTrainingExercises, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>("Failed to save TrainingExercises", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{trainingId}/exercises")
  public ResponseEntity<List<TrainingExercises>> getExercisesByTrainingId(@PathVariable("trainingId") Long trainingId) {
    try {
      List<TrainingExercises> trainingExercises = this.trainingService.getExercisesByTrainingId(trainingId);

      return new ResponseEntity<>(trainingExercises, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }



  @PostMapping("/{trainingId}/validate")
  public ResponseEntity<List<TrainingExercises>> validateTraining(@PathVariable("trainingId") Long trainingId, @Valid @RequestBody ValidateTrainingRequest requestBody) throws Exception {
    try {
      List<TrainingExercises> createdTrainingExercises = this.trainingService.validateTrainingExercises(trainingId, requestBody);

      return new ResponseEntity<>(createdTrainingExercises, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // todo validate patch

  // todo return all template of TrainingExercises and trainingDay = today if exist
  // or return true if series or exercise are done for each exercise and series
}
