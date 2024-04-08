package com.walty.telegram.web.telegram.command.impl;

import com.walty.telegram.web.telegram.command.Command;
import org.springframework.stereotype.Component;

@Component
public class DefaultResponseCommand implements Command {

    private static final String DEFAULT_RESPONSE_TO_UNKNOWN_COMMAND = "Unknown command, please use /help for available commands";

    @Override
    public String execute(long telegramChatId, String input) {
        return DEFAULT_RESPONSE_TO_UNKNOWN_COMMAND;
    }
}
