package com.walty.currency.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Getter
@RefreshScope
@Configuration
public class CurrencyExchangeServiceConfig {

    @Value("${currency.integration-api-url}")
    private String integrationApiUrl;

    @Value("${currency.conversion-percentage-adjustment}")
    private double conversionPercentageAdjustment;

    @Value("${currency.rounding-scale}")
    private int roundingScale;

    @Bean
    public WebClient currencyExchangeApiClient() {
        return WebClient.create(integrationApiUrl);
    }
}
