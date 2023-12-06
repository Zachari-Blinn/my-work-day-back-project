package com.blinnproject.myworkdayback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.Objects;

@Component
public class ActiveProfile {

  private static final Logger logger = LoggerFactory.getLogger(ActiveProfile.class);

  @Value("${spring.profiles.active}")
  private String profile;

  @EventListener(ContextRefreshedEvent.class)
  public void onStartUp() {
    logger.warn("--------------------------------------------------------------------------------------------");
    logger.warn("The current profile is : " +  profile);
    if (!Objects.equals(profile, "prod")) {
      logger.warn("PLEASE RESTART WITH THE PROD PROFILE ARGS IF YOU WANT TO LAUNCH PROJECT IN PRODUCTION MODE");
    }
    logger.warn("--------------------------------------------------------------------------------------------");
  }
}