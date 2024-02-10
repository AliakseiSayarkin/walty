package com.walty.currency.service.impl;

import com.walty.currency.Currency;
import com.walty.currency.service.CurrencyExchangeService;
import com.walty.currency.service.integration.dto.CurrencyExchangeRateDTO;
import com.walty.currency.service.model.CurrencyModel;
import com.walty.currency.service.util.RoundingUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultCurrencyExchangeService implements CurrencyExchangeService {

    @Value("${currency.conversion-percentage-adjustment}")
    private double conversionPercentageAdjustment;

    @NonNull
    private RoundingUtil roundingUtil;


    @Override
    public Optional<CurrencyModel> exchangeCurrency(CurrencyModel currencyToSell, Currency currencyToBuy, CurrencyExchangeRateDTO exchangeRate) {
        if(isNull(currencyToSell) || isNull(currencyToBuy) || isNull(exchangeRate)) {
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
        boughtCurrency.setValue(boughtCurrency.getValue() * conversionPercentageAdjustment);
    }
}
