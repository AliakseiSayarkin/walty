package com.walty.currency.facade.impl;

import com.walty.currency.Currency;
import com.walty.currency.facade.CurrencyExchangeFacade;
import com.walty.currency.facade.converter.CurrencyCodeConverter;
import com.walty.currency.facade.converter.CurrencyDTOConverter;
import com.walty.currency.facade.converter.CurrencyModelConverter;
import com.walty.currency.service.CurrencyExchangeService;
import com.walty.currency.service.integration.CurrencyExchangeIntegrationService;
import com.walty.currency.service.integration.dto.CurrencyExchangeRateDTO;
import com.walty.currency.service.util.RoundingUtil;
import com.walty.currency.web.dto.CurrencyDTO;
import com.walty.currency.web.dto.CurrencyValuesDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.walty.currency.Currency.*;
import static java.lang.Double.parseDouble;
import static java.util.Objects.isNull;

@Slf4j
@Service
@AllArgsConstructor
public class DefaultCurrencyExchangeFacade implements CurrencyExchangeFacade {

    private static final String CURRENCY_EXCHANGE_FALLBACK_VALUE = "0";

    private RoundingUtil roundingUtil;
    private final CurrencyExchangeService currencyExchangeService;
    private final CurrencyExchangeIntegrationService currencyExchangeIntegrationService;

    private final CurrencyDTOConverter currencyDTOConverter;
    private final CurrencyCodeConverter currencyCodeConverter;
    private final CurrencyModelConverter currencyModelConverter;

    @Override
    public Optional<CurrencyDTO> exchangeCurrency(CurrencyDTO currencyToSell, String currencyToBuyCode) {
        if (isNull(currencyToSell) || isNull(currencyToBuyCode)) {
            return Optional.empty();
        }

        var currencyToBuyEnum = currencyCodeConverter.convert(currencyToSell.getCode());
        Optional<CurrencyExchangeRateDTO> currencyExchangeRateOptional = currencyExchangeIntegrationService.getCurrencyExchangeRate(currencyToBuyEnum);
        if (currencyExchangeRateOptional.isEmpty()) {
            log.error("Exchange rate not available for {}", currencyToBuyEnum);
            return Optional.empty();
        }

        return convertCurrency(currencyToSell, currencyToBuyCode, currencyExchangeRateOptional.get());
    }

    private Optional<CurrencyDTO> convertCurrency(CurrencyDTO currencyToSell, String currencyToBuyCode, CurrencyExchangeRateDTO currencyExchangeRate) {
        return currencyExchangeService
                .exchangeCurrency(currencyModelConverter.convert(currencyToSell), currencyCodeConverter.convert(currencyToBuyCode), currencyExchangeRate)
                .map(currencyDTOConverter::convert);
    }

    @Override
    public Optional<CurrencyValuesDTO> createCurrencyValues(CurrencyDTO currencyToSell) {
        if (isNull(currencyToSell)) {
            return Optional.empty();
        }

        var currencyToBuyEnum = currencyCodeConverter.convert(currencyToSell.getCode());
        Optional<CurrencyExchangeRateDTO> currencyExchangeRateOptional = currencyExchangeIntegrationService.getCurrencyExchangeRate(currencyToBuyEnum);
        if (currencyExchangeRateOptional.isEmpty()) {
            log.error("Exchange rate not available for {}", currencyToBuyEnum);
            return Optional.empty();
        }

        var exchangeRate = currencyExchangeRateOptional.get();
        var value = currencyToSell.getValue();
        return Optional.of(
                CurrencyValuesDTO.builder()
                        .usd(applyExchangeRate(exchangeRate, USD, value))
                        .eur(applyExchangeRate(exchangeRate, EUR, value))
                        .byn(applyExchangeRate(exchangeRate, BYN, value))
                        .rub(applyExchangeRate(exchangeRate, RUB, value))
                        .build()
        );
    }

    private double applyExchangeRate(CurrencyExchangeRateDTO exchangeRate, Currency targetCurrency, double value) {
        var appliedExchangeValue = parseDouble(exchangeRate.exchangeRateFor(targetCurrency).orElse(CURRENCY_EXCHANGE_FALLBACK_VALUE)) * value;
        return roundingUtil.round(appliedExchangeValue);
    }
}
