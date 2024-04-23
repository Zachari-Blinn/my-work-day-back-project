package com.blinnproject.myworkdayback.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ProvidedResourceIsNotAnInstanceOfException extends RuntimeException {
  private final String providedResourceName;
  private final String providedResourceValue;
  private final String instanceRequired;

  public ProvidedResourceIsNotAnInstanceOfException(String providedResourceName, String providedResourceValue, String instanceRequired) {
    super(String.format("%s with value %s is not an instance of %s", providedResourceName, providedResourceValue, instanceRequired));
    this.providedResourceName = providedResourceName;
    this.providedResourceValue = providedResourceValue;
    this.instanceRequired = instanceRequired;
  }

  public ProvidedResourceIsNotAnInstanceOfException() {
    super("Provided resource is not an instance of required instance");
    this.providedResourceName = "";
    this.providedResourceValue = "";
    this.instanceRequired = "";
  }
}
