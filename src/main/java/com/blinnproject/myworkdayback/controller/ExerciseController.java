package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.model.Exercise;
import com.blinnproject.myworkdayback.service.exercise.ExerciseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class ExerciseController {

  @Autowired
  private final ExerciseService exerciseService;

  @PostMapping("exercises")
  public Exercise create(@RequestBody Exercise exercise) {
    return exerciseService.create(exercise);
  }
}
