package com.walty.transaction.service.mapper;

import com.walty.transaction.service.model.TransactionRecordModel;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

@Component
public final class TransactionRecordValidFieldsMapper {

    public void mapValidFields(TransactionRecordModel source, TransactionRecordModel target) {
        requireNonNull(source, "Parameter 'source' cannot be null");
        requireNonNull(target, "Parameter 'target' cannot be null");

        mapTelegramChatId(source, target);
        mapDate(source, target);
        mapDescription(source, target);
        mapCurrencies(source, target);
    }

    private void mapTelegramChatId(TransactionRecordModel source, TransactionRecordModel target) {
        target.setTelegramChatId(nonNull(source.getTelegramChatId()) ? source.getTelegramChatId() : target.getTelegramChatId());
    }

    private void mapDate(TransactionRecordModel source, TransactionRecordModel target) {
        target.setDate(nonNull(source.getDate()) ? source.getDate() : target.getDate());
    }

    private void mapDescription(TransactionRecordModel source, TransactionRecordModel target) {
        target.setDescription(StringUtils.hasLength(source.getDescription()) ? source.getDescription() : target.getDescription());
    }

    private void mapCurrencies(TransactionRecordModel source, TransactionRecordModel target) {
        target.setUsd(source.getUsd() > 0 ? source.getUsd() : target.getUsd());
        target.setEur(source.getEur() > 0 ? source.getEur() : target.getEur());
        target.setByn(source.getByn() > 0 ? source.getByn() : target.getByn());
        target.setRub(source.getRub() > 0 ? source.getRub() : target.getRub());
    }
}
