package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.model.dto.ExerciseCreateDTO;
import com.blinnproject.myworkdayback.model.entity.Exercise;
import com.blinnproject.myworkdayback.model.response.GenericResponse;
import com.blinnproject.myworkdayback.security.UserDetailsImpl;
import com.blinnproject.myworkdayback.service.i18n.I18nService;
import com.blinnproject.myworkdayback.service.exercise.ExerciseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name="Exercise", description = "Endpoints related to exercises.")
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/api/exercise", produces="application/json")
public class ExerciseController {

  private final I18nService i18n;
  private final ExerciseService exerciseService;

  public ExerciseController(I18nService i18nService, ExerciseService exerciseService) {
    this.i18n = i18nService;
    this.exerciseService = exerciseService;
  }

  @Operation(summary = "Create Exercise", description = "Creates a new exercise.")
  @PostMapping()
  public ResponseEntity<GenericResponse<Exercise>> create(@Valid @RequestBody ExerciseCreateDTO exerciseCreateDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Exercise exercise = exerciseService.create(exerciseCreateDTO, userDetails.getId());

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(GenericResponse.success(exercise, i18n.translate("controller.exercise.create.successful")));
  }

  @Operation(summary = "Get exercise by ID", description = "Retrieves a exercise by its ID.")
  @GetMapping("/{id}")
  public ResponseEntity<GenericResponse<Exercise>> getExerciseById(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Optional<Exercise> exercise = exerciseService.findById(id, userDetails.getId());

    return exercise.map(model -> ResponseEntity.ok(GenericResponse.success(model, i18n.translate("controller.exercise.return-by-id.successful")))).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @Operation(summary = "Get all exercise", description = "Retrieves all exercise.")
  @GetMapping()
  public ResponseEntity<GenericResponse<List<Exercise>>> getAllExercise(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(GenericResponse.success(exerciseService.findAll(userDetails.getId()), i18n.translate("controller.exercise.return-all.successful")));
  }

  @Operation(summary = "Update exercise", description = "Updates a exercise.")
  @PutMapping("/{id}")
  public ResponseEntity<GenericResponse<Exercise>> update(@PathVariable("id") Long id, @Valid @RequestBody ExerciseCreateDTO exerciseCreateDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Optional<Exercise> exercise = exerciseService.update(id, exerciseCreateDTO, userDetails.getId());

    return exercise.map(model -> ResponseEntity.ok(GenericResponse.success(model, i18n.translate("controller.exercise.update.successful")))).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @Operation(summary = "Delete exercise", description = "Deletes a exercise.")
  @DeleteMapping("/{id}")
  public ResponseEntity<GenericResponse<Void>> delete(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    exerciseService.delete(id, userDetails.getId());

    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body(GenericResponse.success(null, i18n.translate("controller.exercise.delete.successful")));
  }
}
