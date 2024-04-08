package com.walty.telegram.web.telegram;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.walty.telegram.web.telegram.command.CommandResolver;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class WaltyUpdatesListener implements UpdatesListener {

    private final CommandResolver commandResolver;
    private WaltyTelegramBotEngine waltyTelegramBotEngine;

    @Override
    public int process(List<Update> updates) {
        var input = updates.getLast().message().text();
        var chatId = updates.getLast().message().chat().id();

        var command = commandResolver.resolve(input);

        waltyTelegramBotEngine.execute(new SendMessage(chatId, command.execute(chatId, input)));

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
