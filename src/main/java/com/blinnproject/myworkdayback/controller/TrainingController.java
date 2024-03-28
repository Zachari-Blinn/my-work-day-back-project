package com.blinnproject.myworkdayback.controller;

import com.blinnproject.myworkdayback.model.entity.TrainingExercises;
import com.blinnproject.myworkdayback.model.entity.Training;
import com.blinnproject.myworkdayback.payload.dto.training.TrainingCreateDTO;
import com.blinnproject.myworkdayback.payload.dto.training_exercises.TrainingExercisesCreateDTO;
import com.blinnproject.myworkdayback.payload.query.TrainingCalendarDTO;
import com.blinnproject.myworkdayback.payload.response.GenericResponse;
import com.blinnproject.myworkdayback.security.UserDetailsImpl;
import com.blinnproject.myworkdayback.service.i18n.I18nService;
import com.blinnproject.myworkdayback.service.training.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name="Training", description = "Endpoints related to user training.")
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/training")
public class TrainingController {

  private final I18nService i18n;
  private final TrainingService trainingService;

  public TrainingController(I18nService i18nService, TrainingService trainingService) {
    this.i18n = i18nService;
    this.trainingService = trainingService;
  }

  @Operation(summary = "Create Training", description = "Creates a new training.")
  @PostMapping()
  public ResponseEntity<GenericResponse<Training>> create(@Valid @RequestBody TrainingCreateDTO trainingDTO) {
    Training training = trainingService.create(trainingDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(GenericResponse.success(training, i18n.translate("controller.training.create.successful")));
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.0", forRemoval = false)
  @GetMapping("/current-user")
  public ResponseEntity<GenericResponse<List<Training>>> getCurrentUserTrainings() {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    List<Training> trainings = trainingService.getAllTrainingsByCreatedBy(userDetails.getId());

    if (trainings.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return ResponseEntity.ok(GenericResponse.success(trainings, i18n.translate("controller.training.return-all-trainings.successful")));
  }

  @Operation(summary = "Get Training by ID", description = "Retrieves a training by its ID.")
  @GetMapping("/{id}")
  public ResponseEntity<GenericResponse<Training>> getTrainingById(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Optional<Training> trainingData = trainingService.findById(id, userDetails.getId());

    return trainingData.map(training -> ResponseEntity.ok(GenericResponse.success(training, i18n.translate("controller.training.return-training-by-id.successful")))).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @Operation(summary = "Add Exercise to Training", description = "Adds an exercise to a training.")
  @PostMapping("/{trainingId}/exercise/{exerciseId}")
  public ResponseEntity<GenericResponse<TrainingExercises>> addExerciseToTraining(
      @PathVariable("trainingId") Long trainingId,
      @PathVariable("exerciseId") Long exerciseId,
      @Valid @RequestBody TrainingExercisesCreateDTO trainingExercisesDTO,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    TrainingExercises createdTrainingExercises = this.trainingService.addExercise(trainingId, exerciseId, trainingExercisesDTO, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(createdTrainingExercises, i18n.translate("controller.training.add-exercise.successful")));
  }

  // todo move to exercise controller ?
  @Operation(summary = "Get Exercises by Training ID", description = "Retrieves exercises associated with a training by its ID.")
  @GetMapping("/{trainingId}/exercises")
  public ResponseEntity<GenericResponse<List<TrainingExercises>>> getExercisesByTrainingId(
      @RequestParam(defaultValue = "false") Boolean fetchTemplate,
      @PathVariable("trainingId") Long trainingId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    List<TrainingExercises> trainingExercises;

    if (Boolean.TRUE.equals(fetchTemplate)) {
      trainingExercises = this.trainingService.getTemplateExercisesByTrainingId(trainingId, userDetails.getId());
    } else {
      trainingExercises = this.trainingService.getExercisesByTrainingId(trainingId, userDetails.getId());
    }

    return ResponseEntity.ok(GenericResponse.success(trainingExercises, i18n.translate("controller.training.return-training-exercise.successful")));
  }

  @Operation(summary = "Return Training Calendar Info", description = "Retrieves training calendar information within a specified date range.")
  @GetMapping("/calendar/{startDate}/{endDate}")
  public ResponseEntity<GenericResponse<List<TrainingCalendarDTO>>> returnTrainingCalendarInfo(
      @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
      @PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) throws Exception {
    List<TrainingCalendarDTO> result = this.trainingService.getTrainingCalendarInfo(startDate, endDate, userDetails.getId());

    return ResponseEntity.ok(GenericResponse.success(result, i18n.translate("controller.training.return-calendar-info.successful")));
  }
}
