package com.walty.currency.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CurrencyExchangeServiceConfig {

    @Value("${currency.integration-api-url}")
    private String integrationApiUrl;

    @Bean
    public WebClient currencyExchangeApiClient() {
        return WebClient.create(integrationApiUrl);
    }
}
