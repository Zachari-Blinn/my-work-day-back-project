package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.projection.CombinedWorkoutInfoDTO;
import com.blinnproject.myworkdayback.model.projection.WorkoutScheduleDTO;
import com.blinnproject.myworkdayback.model.entity.WorkoutModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutModelRepository extends JpaRepository<WorkoutModel, Long> {
  Optional<WorkoutModel> findByName(String name);

  Optional<WorkoutModel> findByIdAndCreatedBy(Long id, Long createdBy);

  ArrayList<WorkoutModel> findAllByCreatedBy(Long createdBy);
}