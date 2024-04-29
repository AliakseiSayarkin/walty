package com.walty.telegram.web.integration;

import com.walty.telegram.config.TelegramBotServiceConfig;
import com.walty.telegram.web.dto.TransactionRecordDTO;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class TransactionRecordWebClient {

    private WebClient.Builder loadBalancedWebClientBuilder;
    private TelegramBotServiceConfig telegramBotServiceConfig;

    public List<TransactionRecordDTO> getTransactionRecordsUri(String telegramChatId) {
        return loadBalancedWebClientBuilder.build()
                .method(HttpMethod.GET)
                .uri(createGetTransactionRecordsUri(telegramChatId))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<TransactionRecordDTO>>() { })
                .retry(telegramBotServiceConfig.getRetry())
                .block();
    }

    private Function<UriBuilder, URI> createGetTransactionRecordsUri(String telegramChatId) {
        return builder -> builder.scheme("http")
                .host(telegramBotServiceConfig.getTransactionRecordServiceName())
                .path(telegramBotServiceConfig.getGetTransactionRecordsUrl())
                .build(telegramChatId);
    }

    public TransactionRecordDTO saveTransactionRecord(String telegramChatId, TransactionRecordDTO transactionRecord) {
        return loadBalancedWebClientBuilder.build()
                .method(HttpMethod.POST)
                .uri(createSaveTransactionRecordUri(telegramChatId))
                .body(Mono.just(transactionRecord), TransactionRecordDTO.class)
                .retrieve()
                .bodyToMono(TransactionRecordDTO.class)
                .retry(telegramBotServiceConfig.getRetry())
                .block();
    }

    private Function<UriBuilder, URI> createSaveTransactionRecordUri(String telegramChatId) {
        return builder -> builder.scheme("http")
                .host(telegramBotServiceConfig.getTransactionRecordServiceName())
                .path(telegramBotServiceConfig.getSaveTransactionRecordUrl())
                .build(telegramChatId);
    }

    public void deleteAllTransactionRecord(String telegramChatId) {
        loadBalancedWebClientBuilder.build()
                .method(HttpMethod.DELETE)
                .uri(createDeleteAllTransactionRecordsUri(telegramChatId))
                .retrieve()
                .bodyToMono(Void.class)
                .retry(telegramBotServiceConfig.getRetry())
                .block();
    }

    private Function<UriBuilder, URI> createDeleteAllTransactionRecordsUri(String telegramChatId) {
        return builder -> builder.scheme("http")
                .host(telegramBotServiceConfig.getTransactionRecordServiceName())
                .path(telegramBotServiceConfig.getDeleteAllTransactionRecordsUrl())
                .build(telegramChatId);
    }

    public void deleteTransactionRecordById(String id) {
        loadBalancedWebClientBuilder.build()
                .method(HttpMethod.DELETE)
                .uri(createDeleteTransactionRecordByIdUri(id))
                .retrieve()
                .bodyToMono(Void.class)
                .retry(telegramBotServiceConfig.getRetry())
                .block();
    }

    private Function<UriBuilder, URI> createDeleteTransactionRecordByIdUri(String id) {
        return builder -> builder.scheme("http")
                .host(telegramBotServiceConfig.getTransactionRecordServiceName())
                .path(telegramBotServiceConfig.getDeleteTransactionRecordByIdUrl())
                .build(id);
    }
}
