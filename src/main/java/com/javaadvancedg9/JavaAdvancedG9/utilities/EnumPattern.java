package com.javaadvancedg9.JavaAdvancedG9.utilities;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = EnumPatternValidator.class)
public @interface EnumPattern {
    String name();
    String regexp();
    String message() default "{name} must match {regexp}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
