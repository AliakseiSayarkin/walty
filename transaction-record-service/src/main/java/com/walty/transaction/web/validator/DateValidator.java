package com.walty.transaction.web.validator;

import org.springframework.stereotype.Component;

import java.util.Date;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Component
public class DateValidator {
    public void validate(Date date) {
        requireNonNull(date, "Parameter 'date' cannot be null");

        var currentDate = new Date();
        if(currentDate.before(date)) {
            throw new IllegalArgumentException(format("Invalid date: '%s' is in the future", date));
        }
    }
}
