package org.lumeninvestiga.backend.repositorio.tpi.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidUsernameValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {
    String message() default "Invalid username. Must be in the format YYYYXXXX with YYYY between 2000 and 2024.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
