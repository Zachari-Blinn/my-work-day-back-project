package com.blinnproject.myworkdayback.service.mapper;

import com.blinnproject.myworkdayback.model.dto.WorkoutModelCreateDTO;
import com.blinnproject.myworkdayback.model.entity.WorkoutModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class WorkoutModelMapper {

  private final ModelMapper modelMapper;

  public WorkoutModelMapper(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public WorkoutModel toEntity(WorkoutModelCreateDTO workoutModelCreateDTO) {
    return modelMapper.map(workoutModelCreateDTO, WorkoutModel.class);
  }

  public WorkoutModel updateEntityFromDTO(WorkoutModelCreateDTO workoutModelCreateDTO, WorkoutModel workoutModel) {
    modelMapper.map(workoutModelCreateDTO, workoutModel);
    return workoutModel;
  }
}
