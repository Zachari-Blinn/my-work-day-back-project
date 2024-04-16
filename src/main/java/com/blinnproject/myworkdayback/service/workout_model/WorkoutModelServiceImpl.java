package com.blinnproject.myworkdayback.service.workout_model;

import com.blinnproject.myworkdayback.exception.ResourceNotFoundException;
import com.blinnproject.myworkdayback.exception.WorkoutModelWithCurrentUserNotFoundException;
import com.blinnproject.myworkdayback.model.dto.*;
import com.blinnproject.myworkdayback.model.entity.Exercise;
import com.blinnproject.myworkdayback.model.entity.Schedule;
import com.blinnproject.myworkdayback.model.entity.WorkoutExercise;
import com.blinnproject.myworkdayback.model.entity.WorkoutModel;
import com.blinnproject.myworkdayback.repository.WorkoutModelRepository;
import com.blinnproject.myworkdayback.service.exercise.ExerciseService;
import com.blinnproject.myworkdayback.service.schedule.ScheduleService;
import com.blinnproject.myworkdayback.service.workout_exercise.WorkoutExerciseService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class WorkoutModelServiceImpl implements WorkoutModelService {

  private final WorkoutModelRepository workoutModelRepository;
  private final ExerciseService exerciseService;
  private final WorkoutExerciseService workoutExerciseService;
  private final ScheduleService scheduleService;
  private final ModelMapper modelMapper;

  public WorkoutModelServiceImpl(WorkoutModelRepository workoutModelRepository, ExerciseService exerciseService, WorkoutExerciseService workoutExerciseService, ScheduleService scheduleService, ModelMapper modelMapper) {
    this.workoutModelRepository = workoutModelRepository;
    this.exerciseService = exerciseService;
    this.workoutExerciseService = workoutExerciseService;
    this.scheduleService = scheduleService;
    this.modelMapper = modelMapper;
  }

  @Override
  public WorkoutModel create(WorkoutModelCreateDTO workoutModelCreateDTO, Long createdBy) {
    WorkoutModel workoutModel = modelMapper.map(workoutModelCreateDTO, WorkoutModel.class);
    workoutModel.setCreatedBy(createdBy);

    return workoutModelRepository.save(workoutModel);
  }

  @Override
  public Optional<WorkoutModel> findById(Long id, Long createdBy) {
    return workoutModelRepository.findByIdAndCreatedBy(id, createdBy);
  }

  @Override
  public WorkoutExercise addExercise(Long workoutModelId, Long exerciseId, WorkoutExerciseCreateDTO workoutExerciseCreateDTO, Long createdBy) {
    WorkoutModel workoutModel = getWorkoutModelOrThrowError(workoutModelId, createdBy);
    Exercise exercise = exerciseService.findById(exerciseId, createdBy).orElseThrow(() -> new ResourceNotFoundException("Exercise", "id", exerciseId));

    WorkoutExercise trainingExercises = new WorkoutExercise(workoutModel, exercise, workoutExerciseCreateDTO);

    return workoutExerciseService.create(trainingExercises);
  }

  @Override
  public WorkoutExercise removeExercise(Long workoutExerciseId, Long createdBy) {
    WorkoutExercise workoutExercise = workoutExerciseService.findByIdAndCreatedBy(workoutExerciseId, createdBy)
        .orElseThrow(ResourceNotFoundException::new);

    workoutExerciseService.delete(workoutExerciseId, createdBy);

    return workoutExercise;
  }

  @Override
  public void delete(Long id, Long createdBy) {
    Optional<WorkoutModel>  workoutModel = workoutModelRepository.findByIdAndCreatedBy(id, createdBy);
    workoutModel.ifPresent(
      wm -> workoutModelRepository.deleteById(wm.getId())
    );
    if(workoutModel.isEmpty()) {
      throw new ResourceNotFoundException();
    }
  }

  @Override
  public ArrayList<WorkoutModel> findAll(Long createdBy) {
    return workoutModelRepository.findAllByCreatedBy(createdBy);
  }

  @Override
  public Optional<WorkoutModel> update(Long id, WorkoutModelCreateDTO workoutModelCreateDTO, Long createdBy) {
    Optional<WorkoutModel> workoutModelData = workoutModelRepository.findByIdAndCreatedBy(id, createdBy);

    return workoutModelData.map(workoutModel -> {
      modelMapper.map(workoutModelCreateDTO, workoutModel);
      return Optional.of(workoutModelRepository.save(workoutModel));
    }).orElse(Optional.empty());
  }

  @Override
  public ArrayList<WorkoutExercise> findAllExercises(Long id, Long createdBy) {
    return workoutExerciseService.findAllByWorkoutId(id, createdBy);
  }

  @Override
  public Schedule addSchedule(Long workoutModelId, ScheduleCreateDTO scheduleCreateDTO, Long createdBy) {
    return workoutModelRepository.findByIdAndCreatedBy(workoutModelId, createdBy)
        .map(workoutModel -> {
          Schedule newSchedule = modelMapper.map(scheduleCreateDTO, Schedule.class);

          workoutModel.getSchedules().add(newSchedule);
          workoutModelRepository.save(workoutModel);

          return newSchedule;
        })
        .orElseThrow(() -> new ResourceNotFoundException("WorkoutModel", "id", workoutModelId));
  }

  @Override
  public void removeSchedule(Long scheduleId, Long createdBy) {
    scheduleService.delete(scheduleId, createdBy);
  }

  @Override
  public Schedule updateSchedule(Long scheduleId, ScheduleCreateDTO scheduleCreateDTO, Long createdBy) {
    Schedule schedule = modelMapper.map(scheduleCreateDTO, Schedule.class);
    return scheduleService.update(scheduleId, schedule, createdBy);
  }

  @Override
  public WorkoutExercise updateExercise(Long workoutExerciseId, WorkoutExerciseUpdateDTO workoutExerciseUpdateDTO, Long createdBy) {
    WorkoutExercise workoutExercise = modelMapper.map(workoutExerciseUpdateDTO, WorkoutExercise.class);
    return workoutExerciseService.update(workoutExerciseId, workoutExercise, createdBy);
  }

  /**
   * PRIVATE METHODS
   */

  private WorkoutModel getWorkoutModelOrThrowError(Long workoutModelId, Long createdBy) {
    return workoutModelRepository.findByIdAndCreatedBy(workoutModelId, createdBy)
        .orElseThrow(() -> new WorkoutModelWithCurrentUserNotFoundException("Workout model with id " + workoutModelId + " does not belong to current user"));
  }

}
