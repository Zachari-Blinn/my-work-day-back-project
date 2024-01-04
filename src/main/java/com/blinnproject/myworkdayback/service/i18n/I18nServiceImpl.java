package com.blinnproject.myworkdayback.service.i18n;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
public class I18nServiceImpl implements I18nService {

  private final ResourceBundleMessageSource messageSource;

  public I18nServiceImpl(ResourceBundleMessageSource messageSource) {
    this.messageSource = messageSource;
  }

  private final Locale locale = Locale.FRENCH;

  public String getMessage(String key) {
    return getMessage(key, "");
  }

  public String getMessage(String key, String value) {
    List<String> listOfValues = Collections.singletonList(value);
    return getMessage(key, listOfValues);
  }

  public String getMessage(String key, List<String> args) {
    return messageSource.getMessage(key, args.toArray(), locale);
  }

  public String getMessage(String key, List<String> args, Locale requestedLocale) {
    if (requestedLocale == null){
      return getMessage(key, args);
    } else {
      return messageSource.getMessage(key, args.toArray(), requestedLocale);
    }
  }

  public String translate(String key) {
    return translate(key, new ArrayList<>());
  }

  public String translate(String key, List<String> args) {
    return getMessage(key, args, LocaleContextHolder.getLocale());
  }
}