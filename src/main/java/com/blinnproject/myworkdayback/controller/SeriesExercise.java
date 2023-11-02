package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.service.series_exercise.SeriesExerciseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class SeriesExercise {

  @Autowired
  private final SeriesExerciseService seriesExerciseService;

  @PostMapping("series-exercises")
  public SeriesExercise create(@RequestBody SeriesExercise seriesExercise) {
    return seriesExerciseService.create(seriesExercise);
  }
}
