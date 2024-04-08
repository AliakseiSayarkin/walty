package com.walty.telegram.web.telegram;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.walty.telegram.config.TelegramBotServiceConfig;
import com.walty.telegram.web.telegram.command.CommandResolver;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WaltyTelegramBotEngine extends TelegramBot {

    private final ExceptionHandler waltyExceptionHandler;
    private final CommandResolver commandResolver;

    private final boolean shouldStartBot;

    public WaltyTelegramBotEngine(TelegramBotServiceConfig telegramBotServiceConfig, ExceptionHandler waltyExceptionHandler,
                                  CommandResolver commandResolver) {
        super(telegramBotServiceConfig.getBotToken());

        this.waltyExceptionHandler = waltyExceptionHandler;
        this.commandResolver = commandResolver;

        this.shouldStartBot = telegramBotServiceConfig.isShouldStart();
    }

    @PostConstruct
    public void setup() {
        if (shouldStartBot) {
            this.setUpdatesListener(new WaltyUpdatesListener(commandResolver, this), waltyExceptionHandler);
        }
    }
}
