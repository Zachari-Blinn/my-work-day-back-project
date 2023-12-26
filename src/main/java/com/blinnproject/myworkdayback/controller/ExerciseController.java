package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.model.Exercise;
import com.blinnproject.myworkdayback.payload.response.GenericResponse;
import com.blinnproject.myworkdayback.service.exercise.ExerciseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseController {

  private final ExerciseService exerciseService;

  public ExerciseController(ExerciseService exerciseService) {
    this.exerciseService = exerciseService;
  }

  @PostMapping()
  public Exercise create(@RequestBody Exercise exercise) {
    return exerciseService.create(exercise);
  }

  @GetMapping()
  public ResponseEntity<GenericResponse<List<Exercise>>> listAllExercises() {
    List<Exercise> exercises = new ArrayList<>(exerciseService.findAll());

    if (exercises.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    return ResponseEntity.ok(GenericResponse.success(exercises, "List all exercises successfully!"));
  }
}
