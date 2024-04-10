package com.walty.currency.service.util;

import com.walty.currency.config.CurrencyExchangeServiceConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@AllArgsConstructor
public class RoundingUtil {

    private CurrencyExchangeServiceConfig currencyExchangeServiceConfig;

    public double round(double value) {
        var roundedValue = BigDecimal.valueOf(value).setScale(currencyExchangeServiceConfig.getRoundingScale(), RoundingMode.HALF_EVEN);
        return roundedValue.doubleValue();
    }
}
