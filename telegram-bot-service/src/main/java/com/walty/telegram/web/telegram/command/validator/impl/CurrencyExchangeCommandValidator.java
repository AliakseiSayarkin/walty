package com.walty.telegram.web.telegram.command.validator.impl;

import com.walty.telegram.web.telegram.command.validator.CommandValidator;
import com.walty.telegram.service.ParsingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CurrencyExchangeCommandValidator implements CommandValidator {

    private static final String INVALID_CURRENCY_MESSAGE = "No currency code found, please use /exchange {currency code} (for example USD)";

    private ParsingService parsingService;

    @Override
    public Optional<String> isValid(String input) {
        Objects.requireNonNull(input, "Parameter 'input' cannot be null");

        return hasValidCurrency(input) ? Optional.empty() : Optional.of(INVALID_CURRENCY_MESSAGE);
    }

    private boolean hasValidCurrency(String input) {
        return parsingService.getCurrency(input).isPresent();
    }
}
