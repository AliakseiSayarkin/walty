package com.walty.telegram.web.telegram.command.impl;

import com.walty.telegram.web.telegram.command.Command;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {

    private static final String HELP_RESPONSE = """
                                                Available commands:
                                                /getTransactionRecords - get all transaction records
                                                /saveTransactionRecord - save transaction record
                                                /deleteAllTransactionRecords - delete all transaction records
                                                /deleteTransactionRecordById - delete transaction record record by id
                                                
                                                /exchange - get real-time currency exchange rates
                                                
                                                /help - get all available commands with their description
                                                """;

    @Override
    public String execute(long telegramChatId, String input) {
        return HELP_RESPONSE;
    }
}
