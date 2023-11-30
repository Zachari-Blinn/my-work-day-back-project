package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesRepository extends JpaRepository<Series, Long> {
}
