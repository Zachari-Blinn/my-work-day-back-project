package com.blinnproject.myworkdayback.service.i18n;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("${raise.app.locale}")
  private String systemLanguage;

  private Locale locale;

  @PostConstruct
  private void init() {
    locale = new Locale.Builder().setLocale(Locale.of(systemLanguage)).build();
    LocaleContextHolder.setDefaultLocale(locale);
  }

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

  public String getRequestLocalizedMessage(String key) {
    return getRequestLocalizedMessage(key, new ArrayList<>());
  }

  public String getRequestLocalizedMessage(String key, List<String> args) {
    return getMessage(key, args, LocaleContextHolder.getLocale());
  }
}