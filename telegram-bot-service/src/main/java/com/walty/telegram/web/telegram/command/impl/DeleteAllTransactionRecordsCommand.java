package com.walty.telegram.web.telegram.command.impl;

import com.walty.telegram.web.integration.TransactionRecordWebClient;
import com.walty.telegram.web.telegram.command.Command;
import com.walty.telegram.web.telegram.command.aspect.HandleExceptions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeleteAllTransactionRecordsCommand implements Command {

    private static final String SUCCESS_RESPONSE = "All transaction records were successfully deleted";

    private TransactionRecordWebClient transactionRecordWebClient;

    @Override
    @HandleExceptions
    public String execute(long telegramChatId, String input) {
        transactionRecordWebClient.deleteAllTransactionRecord(String.valueOf(telegramChatId));
        return SUCCESS_RESPONSE;
    }
}
