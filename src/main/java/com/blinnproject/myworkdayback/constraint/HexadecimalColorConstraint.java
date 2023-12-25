package com.blinnproject.myworkdayback.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

@Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Le format hexadécimal requis n'est pas bon pour ce champ")
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface HexadecimalColorConstraint {
  String message() default "Le format hexadécimal requis n'est pas bon pour ce champ";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

