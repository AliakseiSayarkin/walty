package com.walty.telegram.web.telegram.command.impl;

import com.walty.telegram.web.telegram.command.Command;
import com.walty.telegram.web.telegram.command.aspect.HandleExceptions;
import com.walty.telegram.web.dto.CurrencyDTO;
import com.walty.telegram.web.telegram.command.validator.CommandValidator;
import com.walty.telegram.service.ParsingService;
import com.walty.telegram.web.integration.CurrencyExchangeWebClient;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class CurrencyExchangeCommand implements Command {

    private static final String CURRENCY_VALUES_RESPONSE = """
                                                           USD = %s
                                                           EUR = %s
                                                           BYN = %s
                                                           RUB = %s
                                                           """;

    private static final double DEFAULT_CURRENCY_EXCHANGE_VALUE = 1d;

    private ParsingService parsingService;
    private CommandValidator currencyExchangeCommandValidator;

    @Setter
    private CurrencyExchangeWebClient currencyExchangeIntegrationService;

    @Override
    @HandleExceptions
    public String execute(long telegramChatId, String input) {
        Objects.requireNonNull(input, "Parameter 'input' cannot be null");

        var errors = currencyExchangeCommandValidator.isValid(input);
        if (errors.isPresent()) {
            return errors.get();
        }

        var currencyOptional = parsingService.getCurrency(input);
        if (currencyOptional.isEmpty()) {
            return EXCEPTION_MESSAGE_RESPONSE;
        }

        var value = parsingService.getValue(input).orElse(DEFAULT_CURRENCY_EXCHANGE_VALUE);
        return createGetCurrencyValuesResponse(currencyOptional.get().toString(), value);
    }

    private String createGetCurrencyValuesResponse(String code, double value) {
        var currency = CurrencyDTO.builder()
                .code(code)
                .value(value)
                .build();

        var values = currencyExchangeIntegrationService.getCurrencyValues(currency);

        return String.format(CURRENCY_VALUES_RESPONSE, values.getUsd(), values.getEur(), values.getByn(), values.getRub());
    }
}
