package com.walty.currency.facade.converter;

import com.walty.currency.service.model.CurrencyModel;
import com.walty.currency.web.dto.CurrencyDTO;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;

@Component
public class CurrencyDataConverter implements Converter<CurrencyModel, CurrencyDTO> {

    @Override
    public CurrencyDTO convert(@Nonnull CurrencyModel source) {
        requireNonNull(source, "source must not be null");

        return CurrencyDTO.builder()
                .code(source.getCode().toString())
                .value(source.getValue())
                .build();
    }
}
