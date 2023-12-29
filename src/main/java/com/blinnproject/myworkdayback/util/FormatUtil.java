package com.blinnproject.myworkdayback.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class FormatUtil {
  private FormatUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static List<Map<String, Object>> convertJsonToList(String jsonString) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(jsonString, new TypeReference<>() {});
  }

}
