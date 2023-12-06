package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

  List<Training> findAllByCreatedBy(Long createdId);
}
