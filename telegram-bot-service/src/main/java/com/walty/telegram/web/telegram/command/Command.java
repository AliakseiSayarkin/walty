package com.walty.telegram.web.telegram.command;

public interface Command {

    String EXCEPTION_MESSAGE_LOG = "Exception in %s: ";
    String EXCEPTION_MESSAGE_RESPONSE = "Something went wrong please try again";

    String execute(long telegramChatId, String input);
}
