package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
  Optional<Workout> findByName(String name);
}