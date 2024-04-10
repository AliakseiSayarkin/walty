package com.walty.currency.service.impl;

import com.walty.currency.config.Currency;
import com.walty.currency.config.CurrencyExchangeServiceConfig;
import com.walty.currency.service.CurrencyExchangeService;
import com.walty.currency.service.integration.dto.CurrencyExchangeRateDTO;
import com.walty.currency.service.model.CurrencyModel;
import com.walty.currency.service.util.RoundingUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Objects.isNull;

@Slf4j
@Service
@AllArgsConstructor
public class DefaultCurrencyExchangeService implements CurrencyExchangeService {

    private RoundingUtil roundingUtil;
    private CurrencyExchangeServiceConfig currencyExchangeServiceConfig;

    @Override
    public Optional<CurrencyModel> exchangeCurrency(CurrencyModel currencyToSell, Currency currencyToBuy, CurrencyExchangeRateDTO exchangeRate) {
        if (isNull(currencyToSell) || isNull(currencyToBuy) || isNull(exchangeRate)) {
            return Optional.empty();
        }

        var exchangeRateOptional = exchangeRate.exchangeRateFor(currencyToBuy);
        if (exchangeRateOptional.isEmpty()) {
            log.error("Exchange rate for {} is not available", currencyToBuy);
            return Optional.empty();
        }

        var exchangeRateValue = Double.parseDouble(exchangeRateOptional.get());
        var boughtCurrency = new CurrencyModel(currencyToBuy, currencyToSell.getValue() * exchangeRateValue);

        adjustCurrencyForBankConversionFee(boughtCurrency);
        boughtCurrency.setValue(roundingUtil.round(boughtCurrency.getValue()));

        return Optional.of(boughtCurrency);
    }

    private void adjustCurrencyForBankConversionFee(CurrencyModel boughtCurrency) {
        boughtCurrency.setValue(boughtCurrency.getValue() * currencyExchangeServiceConfig.getConversionPercentageAdjustment());
    }
}
