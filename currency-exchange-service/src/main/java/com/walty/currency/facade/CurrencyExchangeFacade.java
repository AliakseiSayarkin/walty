package com.walty.currency.facade;

import com.walty.currency.web.dto.CurrencyDTO;
import com.walty.currency.web.dto.CurrencyValuesDTO;

import java.util.Optional;

public interface CurrencyExchangeFacade {

    Optional<CurrencyDTO> exchangeCurrency(CurrencyDTO currencyToSell, String currencyToBuyCode);
    Optional<CurrencyValuesDTO> createCurrencyValues(CurrencyDTO currencyToSell);
}
