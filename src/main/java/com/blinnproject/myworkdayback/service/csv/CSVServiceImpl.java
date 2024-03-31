package com.blinnproject.myworkdayback.service.csv;

import com.blinnproject.myworkdayback.model.response.TrainingSessionInfoResponse;
import com.blinnproject.myworkdayback.repository.WorkoutSessionRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class CSVServiceImpl implements CSVService {

  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CSVServiceImpl.class);

  private final WorkoutSessionRepository trainingRepository;

  public CSVServiceImpl(WorkoutSessionRepository trainingRepository) {
    this.trainingRepository = trainingRepository;
  }

//  public ByteArrayInputStream trainingSessionToCsv(Long createdBy, Date startDate, Date endDate) throws RuntimeException, IOException {
//    logger.info("trainingSessionToCsv");
//
//    List<TrainingSessionInfoResponse> trainingSessionInfo = trainingRepository.findAllTrainingSession(createdBy, startDate, endDate);
//
//    ByteArrayOutputStream out = new ByteArrayOutputStream();
//    CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), CSVFormat.EXCEL.withFirstRecordAsHeader());
//
//    csvPrinter.printRecord("Training Date", "Training Name", "Exercise Name", "Reps Count", "Weight", "Rest Time");
//
//    for (TrainingSessionInfoResponse trainingSessionInfoResponse : trainingSessionInfo) {
//      List<String> data = Arrays.asList(
//          String.valueOf(trainingSessionInfoResponse.getTrainingDate()),
//          String.valueOf(trainingSessionInfoResponse.getTrainingName()),
//          String.valueOf(trainingSessionInfoResponse.getExerciseName()),
//          String.valueOf(trainingSessionInfoResponse.getRepsCount()),
//          String.valueOf(trainingSessionInfoResponse.getWeight()),
//          String.valueOf(trainingSessionInfoResponse.getRestTime())
//      );
//      csvPrinter.printRecord(data);
//    }
//    csvPrinter.flush();
//    return new ByteArrayInputStream(out.toByteArray());
//  }
}
