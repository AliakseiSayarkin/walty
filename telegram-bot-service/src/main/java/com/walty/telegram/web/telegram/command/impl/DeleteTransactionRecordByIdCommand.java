package com.walty.telegram.web.telegram.command.impl;

import com.walty.telegram.service.ParsingService;
import com.walty.telegram.web.integration.TransactionRecordWebClient;
import com.walty.telegram.web.telegram.command.Command;
import com.walty.telegram.web.telegram.command.aspect.HandleExceptions;
import com.walty.telegram.web.telegram.command.validator.CommandValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class DeleteTransactionRecordByIdCommand implements Command {

    private static final String SUCCESS_RESPONSE = "Transaction record with id: %s successfully deleted";
    private static final String NOT_FOUND_TRANSACTION_RECORD_ID_RESPONSE = "Transaction record id not found";

    private ParsingService parsingService;
    private TransactionRecordWebClient transactionRecordWebClient;

    private CommandValidator deleteTransactionRecordByIdCommandValidator;

    @Override
    @HandleExceptions
    public String execute(long telegramChatId, String input) {
        Objects.requireNonNull(input, "Parameter 'input' cannot be null");

        var errors = deleteTransactionRecordByIdCommandValidator.isValid(input);
        if (errors.isPresent()) {
            return errors.get();
        }

        var transactionRecordIdOptional = parsingService.getTransactionRecordId(input);
        if (transactionRecordIdOptional.isEmpty()) {
            return NOT_FOUND_TRANSACTION_RECORD_ID_RESPONSE;
        }

        transactionRecordWebClient.deleteTransactionRecordById(transactionRecordIdOptional.get());

        return String.format(SUCCESS_RESPONSE, transactionRecordIdOptional.get());
    }
}
