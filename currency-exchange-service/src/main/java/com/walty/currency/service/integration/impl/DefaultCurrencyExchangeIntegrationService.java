package com.walty.currency.service.integration.impl;

import com.walty.currency.config.Currency;
import com.walty.currency.service.integration.CurrencyExchangeIntegrationService;
import com.walty.currency.service.integration.dto.CurrencyExchangeRateDTO;
import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

import static java.util.Objects.isNull;

@Slf4j
@Service
@AllArgsConstructor
public class DefaultCurrencyExchangeIntegrationService implements CurrencyExchangeIntegrationService {

    private final WebClient currencyExchangeApiClient;
    private final Converter<String, Optional<CurrencyExchangeRateDTO>> currencyConverter;

    @Override
    public Optional<CurrencyExchangeRateDTO> getCurrencyExchangeRate(Currency currency) {
        if (isNull(currency)) {
            return Optional.empty();
        }

        var currencyExchange = getCurrencyExchangeFromApi(currency);
        return StringUtil.isNullOrEmpty(currencyExchange) ?
                Optional.empty() :
                currencyConverter.convert(currencyExchange);
    }

    private String getCurrencyExchangeFromApi(Currency currency) {
        try {
            return currencyExchangeApiClient
                    .get()
                    .uri(builder -> builder.path(currency.toString()).build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("Unable to get exchange rates for {}", currency);
            return null;
        }
    }
}
