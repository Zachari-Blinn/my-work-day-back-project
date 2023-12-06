package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.TrainingExercises;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrainingExercisesRepository extends JpaRepository<TrainingExercises, Long> {
    @Query("SELECT te FROM TrainingExercises te WHERE te.training.id = :trainingId AND te.parent = NULL")
    List<TrainingExercises> findTemplateByTrainingId(Long trainingId);

    List<TrainingExercises> findByTrainingId(Long trainingId);
}
