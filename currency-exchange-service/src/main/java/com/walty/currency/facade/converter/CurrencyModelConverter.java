package com.walty.currency.facade.converter;

import com.walty.currency.service.model.CurrencyModel;
import com.walty.currency.web.dto.CurrencyDTO;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;

@Component
@AllArgsConstructor
public class CurrencyModelConverter implements Converter<CurrencyDTO, CurrencyModel> {

    private final CurrencyCodeConverter currencyCodeConverter;

    @Override
    public CurrencyModel convert(@Nonnull CurrencyDTO source) {
        requireNonNull(source, "source must not be null");

        return CurrencyModel.builder()
                .code(currencyCodeConverter.convert(source.getCode()))
                .value(source.getValue())
                .build();
    }
}
