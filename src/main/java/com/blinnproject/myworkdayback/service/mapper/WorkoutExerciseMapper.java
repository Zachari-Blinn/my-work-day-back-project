package com.blinnproject.myworkdayback.service.mapper;

import com.blinnproject.myworkdayback.model.dto.WorkoutExerciseCreateDTO;
import com.blinnproject.myworkdayback.model.entity.WorkoutExercise;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class WorkoutExerciseMapper {

  private final ModelMapper modelMapper;

  public WorkoutExerciseMapper(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public WorkoutExercise toEntity(WorkoutExerciseCreateDTO workoutExerciseCreateDTO) {
    return modelMapper.map(workoutExerciseCreateDTO, WorkoutExercise.class);
  }
}
