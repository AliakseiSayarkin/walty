package com.walty.telegram.web.telegram.command.impl;

import com.walty.telegram.service.ParsingService;
import com.walty.telegram.web.telegram.command.Command;
import com.walty.telegram.web.dto.CurrencyDTO;
import com.walty.telegram.web.dto.CurrencyValuesDTO;
import com.walty.telegram.web.dto.TransactionRecordDTO;
import com.walty.telegram.web.integration.CurrencyExchangeWebClient;
import com.walty.telegram.web.integration.TransactionRecordWebClient;
import com.walty.telegram.web.telegram.command.validator.CommandValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@AllArgsConstructor
public class SaveTransactionRecordCommand implements Command {

    private static final String ERROR_RESPONSE = "Unable to save transaction record please try again later";
    private static final String NO_VALUE_FOUND_ERROR_RESPONSE = "Unable to save transaction record: no value found";
    private static final String NO_CURRENCY_FOUND_ERROR_RESPONSE = "Unable to save transaction record: no currency found";

    private static final String SUCCESS_RESPONSE = "Transaction record is saved";

    private static final String DEFAULT_DESCRIPTION = "Expense";

    private ParsingService parsingService;

    private TransactionRecordWebClient transactionRecordWebClient;
    private CurrencyExchangeWebClient currencyExchangeIntegrationService;

    private CommandValidator saveTransactionRecordCommandValidator;

    @Override
    public String execute(long telegramChatId, String input) {
        Objects.requireNonNull(input, "Parameter 'input' cannot be null");

        var errors = saveTransactionRecordCommandValidator.isValid(input);
        if (errors.isPresent()) {
            return errors.get();
        }

        var currencyOptional = parsingService.getCurrency(input);
        if (currencyOptional.isEmpty()) {
            return NO_CURRENCY_FOUND_ERROR_RESPONSE;
        }

        var valueOptional = parsingService.getValue(input);
        if (valueOptional.isEmpty()) {
            return NO_VALUE_FOUND_ERROR_RESPONSE;
        }

        var currencyValues = getCurrencyValues(currencyOptional.get().toString(), valueOptional.get());
        return createSaveTransactionRecordResponse(telegramChatId, currencyValues, parsingService.getDescription(input));
    }

    private CurrencyValuesDTO getCurrencyValues(String code, double value) {
        var currency = CurrencyDTO.builder()
                .code(code)
                .value(value)
                .build();

        return currencyExchangeIntegrationService.getCurrencyValues(currency);
    }

    private String createSaveTransactionRecordResponse(long telegramChatId, CurrencyValuesDTO currencyValues, Optional<String> descriptionOptional) {
        var transactionRecord = TransactionRecordDTO.builder()
                .telegramChatId(String.valueOf(telegramChatId))
                .usd(currencyValues.getUsd())
                .eur(currencyValues.getEur())
                .rub(currencyValues.getRub())
                .byn(currencyValues.getByn())
                .description(descriptionOptional.orElse(DEFAULT_DESCRIPTION))
                .build();

        var savedTransactionRecord = transactionRecordWebClient.saveTransactionRecord(String.valueOf(telegramChatId), transactionRecord);
        if (savedTransactionRecord == null) {
            return ERROR_RESPONSE;
        }

        return SUCCESS_RESPONSE;
    }
}
