package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.TrainingExercises;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingExercisesRepository extends JpaRepository<TrainingExercises, Long> {
    List<TrainingExercises> findByTrainingId(Long trainingId);
}
