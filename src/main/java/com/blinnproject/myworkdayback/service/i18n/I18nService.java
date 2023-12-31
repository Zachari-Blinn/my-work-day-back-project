package com.blinnproject.myworkdayback.service.i18n;

import java.util.List;
import java.util.Locale;

public interface I18nService {
  String getMessage(String key);

  String getMessage(String key, String value);

  String getMessage(String key, List<String> args);

  String getMessage(String key, List<String> args, Locale requestedLocale);

  String translate(String key);

  String translate(String key, List<String> args);
}
