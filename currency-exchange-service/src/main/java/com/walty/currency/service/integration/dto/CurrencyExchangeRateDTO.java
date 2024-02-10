package com.walty.currency.service.integration.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.walty.currency.Currency;
import lombok.Data;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static com.walty.currency.Currency.*;

@Data
public class CurrencyExchangeRateDTO {

    @JsonSetter("base_code")
    private String code;

    @JsonSetter("USD")
    private String usd;
    @JsonSetter("EUR")
    private String eur;
    @JsonSetter("BYN")
    private String byn;
    @JsonSetter("RUB")
    private String rub;

    private Map<Currency, Supplier<String>> currencyToExchangeRate;

    public CurrencyExchangeRateDTO() {
        currencyToExchangeRate = new EnumMap<>(Currency.class);

        currencyToExchangeRate.put(USD, this::getUsd);
        currencyToExchangeRate.put(EUR, this::getEur);
        currencyToExchangeRate.put(BYN, this::getByn);
        currencyToExchangeRate.put(RUB, this::getRub);
    }

    public Optional<String> exchangeRateFor(Currency currencyCode) {
        return Optional.ofNullable(currencyToExchangeRate.get(currencyCode).get());
    }
}
