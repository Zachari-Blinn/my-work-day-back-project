package com.blinnproject.myworkdayback.util;

import com.blinnproject.myworkdayback.payload.query.TrainingCalendarDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class FormatUtil {
  private FormatUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static List<TrainingCalendarDTO.TrainingDTO> convertJsonToList(String jsonString) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(jsonString, new TypeReference<>() {});
  }
}
