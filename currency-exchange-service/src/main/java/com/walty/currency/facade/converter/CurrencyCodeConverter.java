package com.walty.currency.facade.converter;

import com.walty.currency.config.Currency;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CurrencyCodeConverter implements Converter<String, Currency> {

    @Override
    public Currency convert(@Nonnull String source) {
        return Currency.valueOf(source);
    }
}
