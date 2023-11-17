package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.TrainingExercises;
import com.blinnproject.myworkdayback.model.TrainingExercisesKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingExercisesRepository extends JpaRepository<TrainingExercises, TrainingExercisesKey> {
}
