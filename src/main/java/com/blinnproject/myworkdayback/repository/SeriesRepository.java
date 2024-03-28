package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.entity.WorkoutSet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesRepository extends JpaRepository<WorkoutSet, Long> {
}
