package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.model.*;
import com.blinnproject.myworkdayback.payload.request.AddExerciseRequest;
import com.blinnproject.myworkdayback.repository.TrainingExercisesRepository;
import com.blinnproject.myworkdayback.service.exercise.ExerciseService;
import com.blinnproject.myworkdayback.service.training.TrainingService;
import com.blinnproject.myworkdayback.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/training")
public class TrainingController {

  @Autowired
  TrainingService trainingService;

  @Autowired
  ExerciseService exerciseService;

  @Autowired
  TrainingExercisesRepository trainingExercisesRepository;

  @PostMapping()
  public ResponseEntity<Training> create(@Valid @RequestBody Training trainingRequest) {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = userDetails.getId();
    try {
      Training _training = trainingService.create(userId, trainingRequest);
      return new ResponseEntity<>(_training, HttpStatus.CREATED);
    } catch (Exception exception) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/current-user")
  public ResponseEntity<List<Training>> getCurrentUserTrainings() {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = userDetails.getId();
    try {
      List<Training> trainings = trainingService.getAllTrainingsByUserId(userId);

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
    Optional<Training> trainingData = trainingService.findById(id);

    if (trainingData.isPresent()) {
      return new ResponseEntity<>(trainingData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/{trainingId}/add-exercise")
  public ResponseEntity<?> addExercise(@PathVariable("trainingId") Long trainingId, @Valid @RequestBody AddExerciseRequest addExerciseRequest) {
    Training trainingData = trainingService.findById(trainingId).orElse(null);
    Exercise exerciseData = exerciseService.findById(addExerciseRequest.getExerciseId()).orElse(null);
    // add trainingExercise
    TrainingExercisesKey trainingExercisesKey = new TrainingExercisesKey(trainingId, addExerciseRequest.getExerciseId());
    TrainingExercises trainingExercises = new TrainingExercises(trainingExercisesKey, addExerciseRequest.getNotes(), addExerciseRequest.getNumberOfWarmUpSeries());
    trainingExercises.setTraining(trainingData);
    trainingExercises.setExercise(exerciseData);

    trainingExercisesRepository.save(trainingExercises);

    return new ResponseEntity<>(trainingExercises, HttpStatus.OK);
  }
}
