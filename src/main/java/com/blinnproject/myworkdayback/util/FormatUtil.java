package com.blinnproject.myworkdayback.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Random;

public class FormatUtil {
  private FormatUtil() {
    throw new IllegalStateException("Utility class");
  }

//  public static List<TrainingCalendarDTO.TrainingDTO> convertJsonToList(String jsonString) throws Exception {
//    ObjectMapper objectMapper = new ObjectMapper();
//    return objectMapper.readValue(jsonString, new TypeReference<>() {});
//  }

  public static String generateSixNumbersRandomlyToken() {
    return String.format("%06d", new Random().nextInt(1000000));
  }
}
