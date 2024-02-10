package com.walty.currency.web.validator;

import jakarta.validation.Constraint;

import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CurrencyValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@NotNull(message = "Currency code cannot be null")
@ReportAsSingleViolation
public @interface ValidCurrency {

    String message() default "Unknown currency code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
