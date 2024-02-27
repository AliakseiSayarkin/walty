package com.walty.transaction.facade.converter;

import com.walty.transaction.service.model.TransactionRecordModel;
import com.walty.transaction.web.dto.TransactionRecordDTO;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;

@Component
public class TransactionRecordModelConverter implements Converter<TransactionRecordDTO, TransactionRecordModel> {

    @Override
    public TransactionRecordModel convert(@Nonnull TransactionRecordDTO source) {
        requireNonNull(source, "Parameter 'source' cannot be null");

        return TransactionRecordModel.builder()
                .id(source.getId())
                .telegramChatId(source.getTelegramChatId())
                .date(source.getDate())
                .description(source.getDescription())
                .usd(source.getUsd())
                .eur(source.getEur())
                .byn(source.getByn())
                .rub(source.getRub())
                .build();
    }
}
