package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.model.*;
import com.blinnproject.myworkdayback.payload.request.AddExerciseRequest;
import com.blinnproject.myworkdayback.payload.response.ExerciseWithSeriesResponse;
import com.blinnproject.myworkdayback.repository.SeriesRepository;
import com.blinnproject.myworkdayback.repository.TrainingExercisesRepository;
import com.blinnproject.myworkdayback.service.exercise.ExerciseService;
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
import java.util.stream.Collectors;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/training")
public class TrainingController {

  @Autowired
  TrainingService trainingService;

  @Autowired
  ExerciseService exerciseService;

  @Autowired
  TrainingExercisesRepository trainingExercisesRepository;

  @Autowired
  SeriesRepository seriesRepository;

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
    Optional<Training> trainingData = trainingService.findById(id);

    if (trainingData.isPresent()) {
      return new ResponseEntity<>(trainingData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/{trainingId}/add-exercise")
  public ResponseEntity<?> addExercise(@PathVariable("trainingId") Long trainingId, @Valid @RequestBody AddExerciseRequest addExerciseRequest) {
    try {
      TrainingExercisesKey trainingExercisesKey = new TrainingExercisesKey(trainingId, addExerciseRequest.getExerciseId());
      TrainingExercises trainingExercises = new TrainingExercises(trainingExercisesKey, addExerciseRequest.getNotes(), addExerciseRequest.getNumberOfWarmUpSeries());

      trainingExercises.setTraining(trainingService.findById(trainingId).orElse(null));
      trainingExercises.setExercise(exerciseService.findById(addExerciseRequest.getExerciseId()).orElse(null));

      List<Series> seriesList = addExerciseRequest.getSeries();

//      trainingExercises.addSeries(new HashSet<>(seriesList));

      TrainingExercises createdTrainingExercises = trainingExercisesRepository.save(trainingExercises);
      seriesRepository.saveAll(seriesList);

      return new ResponseEntity<>(createdTrainingExercises, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>("Failed to save TrainingExercises", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{trainingId}/exercises")
  public ResponseEntity<List<TrainingExercises>> getExercisesByTrainingId(@PathVariable("trainingId") Long trainingId) {
    List<TrainingExercises> trainingExercises = trainingExercisesRepository.findByTrainingId(trainingId);

    return new ResponseEntity<>(trainingExercises, HttpStatus.OK);
  }
}
