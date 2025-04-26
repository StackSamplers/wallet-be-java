package com.gucardev.wallet.infrastructure.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.time.LocalDateTime;

public class MaxBookingDurationValidator implements ConstraintValidator<MaxBookingDuration, LocalDateTime> {

    @Value("${booking.max-duration-minutes:30}")
    private Integer maxMinutes;

    @Override
    public boolean isValid(LocalDateTime bookUntilDate, ConstraintValidatorContext context) {
        if (bookUntilDate == null) {
            return true;
        }

        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(now, bookUntilDate);
        return duration.toMinutes() <= maxMinutes;
    }
}
