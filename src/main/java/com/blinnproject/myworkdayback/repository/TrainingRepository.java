package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Long> {
}
