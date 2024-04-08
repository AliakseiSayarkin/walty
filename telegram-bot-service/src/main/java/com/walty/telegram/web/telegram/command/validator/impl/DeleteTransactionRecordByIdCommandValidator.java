package com.walty.telegram.web.telegram.command.validator.impl;

import com.walty.telegram.service.ParsingService;
import com.walty.telegram.web.telegram.command.validator.CommandValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@AllArgsConstructor
public class DeleteTransactionRecordByIdCommandValidator implements CommandValidator {

    private static final String NOT_FOUND_TRANSACTION_RECORD_ID_MESSAGE = "No transaction record id found, please use /deleteTransactionRecordById {transaction record id} (for example 1)";
    private static final String INVALID_TRANSACTION_RECORD_ID_MESSAGE = "Transaction record id must be a single word, please use /deleteTransactionRecordById {transaction record id} (for example 1)";

    private ParsingService parsingService;

    @Override
    public Optional<String> isValid(String input) {
        Objects.requireNonNull(input, "Parameter 'input' cannot be null");

        return Optional.ofNullable(isValidTransactionRecordId(input));
    }

    private String isValidTransactionRecordId(String input) {
        var transactionRecordIdOptional = parsingService.getTransactionRecordId(input);

        if (transactionRecordIdOptional.isEmpty()) {
            return NOT_FOUND_TRANSACTION_RECORD_ID_MESSAGE;
        }

        return isTransactionRecordId(transactionRecordIdOptional.get()) ? null : INVALID_TRANSACTION_RECORD_ID_MESSAGE;
    }

    private boolean isTransactionRecordId(String transactionRecord) {
        return !transactionRecord.trim().contains(" ");
    }
}
