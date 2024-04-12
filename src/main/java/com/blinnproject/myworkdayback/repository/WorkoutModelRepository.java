package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.entity.WorkoutModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface WorkoutModelRepository extends JpaRepository<WorkoutModel, Long> {
  Optional<WorkoutModel> findByName(String name);

  Optional<WorkoutModel> findByIdAndCreatedBy(Long id, Long createdBy);

  ArrayList<WorkoutModel> findAllByCreatedBy(Long createdBy);
}