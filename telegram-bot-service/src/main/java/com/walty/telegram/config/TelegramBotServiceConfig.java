package com.walty.telegram.config;

import com.walty.telegram.web.telegram.command.Command;
import com.walty.telegram.web.telegram.command.CommandType;
import com.walty.telegram.web.telegram.command.impl.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.EnumMap;
import java.util.Map;

@Getter
@RefreshScope
@Configuration
public class TelegramBotServiceConfig {

    @Value("${telegram.bot.username}")
    private String botUsername;
    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.bot.should-start}")
    private boolean shouldStart;

    @Value("${telegram.integration.retry}")
    private int retry;

    @Value("${telegram.integration.currency-exchange.service-name}")
    private String currencyExchangeServiceName;
    @Value("${telegram.integration.currency-exchange.create-currency-values-url}")
    private String getCurrencyValuesUrl;

    @Value("${telegram.integration.transaction-record.service-name}")
    private String transactionRecordServiceName;
    @Value("${telegram.integration.transaction-record.get-transaction-records-url}")
    private String getTransactionRecordsUrl;
    @Value("${telegram.integration.transaction-record.save-transaction-records-url}")
    private String saveTransactionRecordUrl;
    @Value("${telegram.integration.transaction-record.delete-all-transaction-records-url}")
    private String deleteAllTransactionRecordsUrl;
    @Value("${telegram.integration.transaction-record.delete-transaction-record-by-id-url}")
    private String deleteTransactionRecordByIdUrl;

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public Map<CommandType, Command> commands(ApplicationContext context) {
        var commands = new EnumMap<CommandType, Command>(CommandType.class);

        commands.put(CommandType.EXCHANGE, context.getBean(CurrencyExchangeCommand.class));
        commands.put(CommandType.HELP, context.getBean(HelpCommand.class));
        commands.put(CommandType.GET_TRANSACTION_RECORDS, context.getBean(GetTransactionRecordsCommand.class));
        commands.put(CommandType.SAVE_TRANSACTION_RECORDS, context.getBean(SaveTransactionRecordCommand.class));
        commands.put(CommandType.DELETE_ALL_TRANSACTION_RECORDS, context.getBean(DeleteAllTransactionRecordsCommand.class));
        commands.put(CommandType.DELETE_TRANSACTION_RECORD_BY_ID, context.getBean(DeleteTransactionRecordByIdCommand.class));

        return commands;
    }
}
