package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.model.Exercise;
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
@AllArgsConstructor
public class ExerciseController {

  @Autowired
  private final ExerciseService exerciseService;

  @PostMapping()
  public Exercise create(@RequestBody Exercise exercise) {
    return exerciseService.create(exercise);
  }

  @GetMapping()
  public ResponseEntity<List<Exercise>> listAllExercises() {
    try {
      List<Exercise> exercises = new ArrayList<Exercise>(exerciseService.findAll());

      if (exercises.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(exercises, HttpStatus.OK);
    } catch (Exception exception) {
      System.out.println("----------------------" + exception);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
