package com.blinnproject.myworkdayback.util;

import com.blinnproject.myworkdayback.payload.query.TrainingCalendarDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Random;

public class FormatUtil {
  private FormatUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static List<TrainingCalendarDTO.TrainingDTO> convertJsonToList(String jsonString) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(jsonString, new TypeReference<>() {});
  }

  public static String generateRandomFourNumbersToken() {
    return String.format("%04d", new Random().nextInt(10000));
  }
}
