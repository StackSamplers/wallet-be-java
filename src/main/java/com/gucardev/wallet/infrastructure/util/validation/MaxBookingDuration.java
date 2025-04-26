package com.gucardev.wallet.infrastructure.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MaxBookingDurationValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxBookingDuration {

    String message() default "Booking duration cannot exceed the specified limit.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
