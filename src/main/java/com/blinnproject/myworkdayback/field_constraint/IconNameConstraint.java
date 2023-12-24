package com.blinnproject.myworkdayback.field_constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

@Pattern(regexp = "^(icon|emoji)_.*", message = "Le champ iconName doit commencer par 'icon_' ou 'emoji_'")
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface IconNameConstraint {
  String message() default "Le champ iconName doit commencer par 'icon_' ou 'emoji_'";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}