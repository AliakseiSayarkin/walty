package com.walty.telegram.web.telegram.command.validator.impl;

import com.walty.telegram.service.ParsingService;
import com.walty.telegram.web.telegram.command.validator.CommandValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@AllArgsConstructor
public class SaveTransactionRecordCommandValidator implements CommandValidator {

    private static final String VALUE_NOT_FOUND_MESSAGE = "No value found, please use /saveTransactionRecord {currency code} {value} (for example 100)";
    private static final String CURRENCY_NOT_FOUND_MESSAGE = "No currency code found, please use /saveTransactionRecord {currency code} (for example USD) {value}";
    private static final String DESCRIPTION_SHOULD_BE_LAST_MESSAGE = "Description starts with -d and should be last in a command";

    private ParsingService parsingService;

    @Override
    public Optional<String> isValid(String input) {
        Objects.requireNonNull(input, "Parameter 'input' cannot be null");

        var descriptionBeginIndexOptional = parsingService.getDescriptionBeginIndex(input);

        if (descriptionBeginIndexOptional.isPresent()) {
            var formattedInput = input.substring(0, descriptionBeginIndexOptional.get());
            var currencyOptional = parsingService.getCurrency(formattedInput);
            var valueOptional = parsingService.getValue(formattedInput);

            if (currencyOptional.isEmpty() || valueOptional.isEmpty()) {
                return Optional.of(DESCRIPTION_SHOULD_BE_LAST_MESSAGE);
            }
        }

        if (parsingService.getCurrency(input).isEmpty()) {
            return Optional.of(CURRENCY_NOT_FOUND_MESSAGE);
        }
        if (parsingService.getValue(input).isEmpty()) {
            return Optional.of(VALUE_NOT_FOUND_MESSAGE);
        }

        return Optional.empty();
    }
}
