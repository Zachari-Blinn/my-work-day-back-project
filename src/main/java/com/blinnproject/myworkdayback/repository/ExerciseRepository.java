package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
}
