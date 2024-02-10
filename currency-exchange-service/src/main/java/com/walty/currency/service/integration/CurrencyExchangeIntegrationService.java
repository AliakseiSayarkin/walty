package com.walty.currency.service.integration;

import com.walty.currency.Currency;
import com.walty.currency.service.integration.dto.CurrencyExchangeRateDTO;

import java.util.Optional;

public interface CurrencyExchangeIntegrationService {
    Optional<CurrencyExchangeRateDTO> getCurrencyExchangeRate(Currency currency);
}
