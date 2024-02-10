package com.walty.currency.service;

import com.walty.currency.Currency;
import com.walty.currency.service.integration.dto.CurrencyExchangeRateDTO;
import com.walty.currency.service.model.CurrencyModel;

import java.util.Optional;

public interface CurrencyExchangeService {

    Optional<CurrencyModel> exchangeCurrency(CurrencyModel currencyToSell, Currency currencyToBuy, CurrencyExchangeRateDTO exchangeRate);
}
