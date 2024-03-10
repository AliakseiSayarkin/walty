package com.walty.telegram.web.integration;

import com.walty.telegram.config.TelegramBotServiceConfig;
import com.walty.telegram.web.dto.CurrencyDTO;
import com.walty.telegram.web.dto.CurrencyValuesDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class CurrencyExchangeWebClient {

    private WebClient.Builder webClientBuilder;
    private TelegramBotServiceConfig telegramBotServiceConfig;

    public CurrencyValuesDTO getCurrencyValues(CurrencyDTO currencyToSell) {
        return webClientBuilder.build()
                .method(HttpMethod.GET)
                .uri(getCurrencyValuesUri())
                .body(Mono.just(currencyToSell), CurrencyDTO.class)
                .retrieve()
                .bodyToMono(CurrencyValuesDTO.class)
                .retry(telegramBotServiceConfig.getRetry())
                .block();
    }

    private Function<UriBuilder, URI> getCurrencyValuesUri() {
        return builder -> builder.scheme("http")
                .host(telegramBotServiceConfig.getCurrencyExchangeServiceName())
                .path(telegramBotServiceConfig.getGetCurrencyValuesUrl())
                .build();
    }
}
