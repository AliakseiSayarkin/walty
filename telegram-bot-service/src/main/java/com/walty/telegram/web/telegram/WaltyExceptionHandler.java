package com.walty.telegram.web.telegram;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WaltyExceptionHandler implements ExceptionHandler {

    @Override
    public void onException(TelegramException e) {
        log.error("Exception in Walty Telegram Bot: ", e);
    }
}
