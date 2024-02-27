package com.walty.transaction.web.validator;

import com.walty.transaction.web.dto.TransactionRecordDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Component
public class TransactionRecordValidator {

    private static final int MIN_VALUE_FOR_CURRENCY = 0;

    private static final String INVALID_CURRENCY_VALUE_MESSAGE = "Cannot save transaction record because of invalid %s value: %s";

    public void validateForSave(TransactionRecordDTO transactionRecord) {
        requireNonNull(transactionRecord, "Cannot save transaction record because it's null");

        validateIds(transactionRecord);
        validateCurrencies(transactionRecord);
    }

    private void validateIds(TransactionRecordDTO transactionRecord) {
        if (StringUtils.hasLength(transactionRecord.getId())) {
            throw new IllegalArgumentException(
                    format("Cannot save transaction records because it should not have id, but %s is found", transactionRecord.getId())
            );
        }

        validateId(transactionRecord.getTelegramChatId(), "Cannot save transaction record because telegram chat id is empty");
    }

    private void validateCurrencies(TransactionRecordDTO transactionRecord) {
        if (transactionRecord.getUsd() <= MIN_VALUE_FOR_CURRENCY) {
            throw new IllegalArgumentException(format(INVALID_CURRENCY_VALUE_MESSAGE, "usd", transactionRecord.getUsd()));
        }

        if (transactionRecord.getEur() <= MIN_VALUE_FOR_CURRENCY) {
            throw new IllegalArgumentException(format(INVALID_CURRENCY_VALUE_MESSAGE, "eur", transactionRecord.getEur()));
        }

        if (transactionRecord.getByn() <= MIN_VALUE_FOR_CURRENCY) {
            throw new IllegalArgumentException(format(INVALID_CURRENCY_VALUE_MESSAGE, "byn", transactionRecord.getByn()));
        }

        if (transactionRecord.getRub() <= MIN_VALUE_FOR_CURRENCY) {
            throw new IllegalArgumentException(format(INVALID_CURRENCY_VALUE_MESSAGE, "rub", transactionRecord.getRub()));
        }
    }

    public void validateForUpdate(TransactionRecordDTO transactionRecord) {
        requireNonNull(transactionRecord, "Cannot update transaction record because it's null");
        validateId(transactionRecord.getId(), "Cannot update transaction record with empty id");
    }

    public void validateTransactionRecordId(String transactionRecordId) {
        validateId(transactionRecordId, "Transaction record id cannot be empty");
    }

    public void validateTelegramChatId(String telegramChatId) {
        validateId(telegramChatId, "Telegram chat id cannot be empty");
    }

    private void validateId(String id, String message) {
        if(!StringUtils.hasLength(id)) {
            throw new IllegalArgumentException(message);
        }
    }
}
