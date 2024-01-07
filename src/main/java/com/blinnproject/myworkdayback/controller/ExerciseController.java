package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.model.Exercise;
import com.blinnproject.myworkdayback.payload.response.GenericResponse;
import com.blinnproject.myworkdayback.security.UserDetailsImpl;
import com.blinnproject.myworkdayback.service.i18n.I18nService;
import com.blinnproject.myworkdayback.service.exercise.ExerciseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/exercise")
public class ExerciseController {

  private final I18nService i18n;
  private final ExerciseService exerciseService;

  public ExerciseController(I18nService i18nService, ExerciseService exerciseService) {
    this.i18n = i18nService;
    this.exerciseService = exerciseService;
  }

  @PostMapping()
  public Exercise create(@RequestBody Exercise exercise) {
    return exerciseService.create(exercise);
  }

  @GetMapping()
  public ResponseEntity<GenericResponse<List<Exercise>>> listAllExercises() {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    List<Exercise> exercises = new ArrayList<>(exerciseService.findAll(userDetails.getId()));

    if (exercises.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    return ResponseEntity.ok(GenericResponse.success(exercises, i18n.translate("controller.exercise.list-all.successful")));
  }
}
