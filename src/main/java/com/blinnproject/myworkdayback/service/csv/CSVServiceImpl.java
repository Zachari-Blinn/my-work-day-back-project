package com.blinnproject.myworkdayback.service.csv;

import com.blinnproject.myworkdayback.repository.TrainingRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Writer;

@Service
public class CSVServiceImpl implements CSVService {

  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CSVServiceImpl.class);

  private final TrainingRepository trainingRepository;

  public CSVServiceImpl(TrainingRepository trainingRepository) {
    this.trainingRepository = trainingRepository;
  }

  public void trainingSessionToCsv(Writer writer) {
    logger.info("trainingSessionToCsv");
    // TODO document why this method is empty
  }
}
