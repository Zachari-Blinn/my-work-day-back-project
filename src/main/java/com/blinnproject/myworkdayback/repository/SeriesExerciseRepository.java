package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.SeriesExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesExerciseRepository extends JpaRepository<SeriesExercise, Long> {
}
