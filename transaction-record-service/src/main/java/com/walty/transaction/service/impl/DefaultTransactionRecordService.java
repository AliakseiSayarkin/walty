package com.walty.transaction.service.impl;

import com.walty.transaction.dao.TransactionRecordRepository;
import com.walty.transaction.service.TransactionRecordService;
import com.walty.transaction.service.model.TransactionRecordModel;
import com.walty.transaction.service.mapper.TransactionRecordValidFieldsMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Date;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.*;

@Service
@AllArgsConstructor
public class DefaultTransactionRecordService implements TransactionRecordService {

    private static final String NULL_DATE_ERROR_MESSAGE = "Parameter 'date' cannot be null";
    private static final String NULL_TELEGRAM_CHAT_ID_ERROR_MESSAGE = "Parameter 'telegramChatId' cannot be null";
    private static final String NULL_TRANSACTION_RECORD_ERROR_MESSAGE = "Parameter 'transactionRecord' cannot be null";
    private static final String NULL_TRANSACTION_RECORD_ID_ERROR_MESSAGE = "Parameter 'transactionRecordId' cannot be null";
    private static final String NOT_FOUND_TRANSACTION_RECORD_ERROR_MESSAGE = "Transaction record with Id '%s' not found";
    private static final String NOT_FOUND_TELEGRAM_CHAT_ID_ERROR_MESSAGE = "Transaction records with telegram chat Id '%s' not found";

    private static final ZoneOffset MINSK_TIME = ZoneOffset.of("+03:00");

    private TransactionRecordRepository transactionRecordRepository;
    private TransactionRecordValidFieldsMapper transactionRecordValidFieldsMapper;

    @Override
    public List<TransactionRecordModel> getTransactionRecordsByTelegramChatIdAndDate(String telegramChatId, Date date) {
        requireNonNull(telegramChatId, NULL_TELEGRAM_CHAT_ID_ERROR_MESSAGE);
        requireNonNull(date, NULL_DATE_ERROR_MESSAGE);

        return transactionRecordRepository.findAllByTelegramChatIdAndDateGreaterThanEqualOrderByDateAsc(telegramChatId, date);
    }

    @Override
    public TransactionRecordModel saveTransactionRecord(TransactionRecordModel transactionRecord) {
        requireNonNull(transactionRecord, NULL_TRANSACTION_RECORD_ERROR_MESSAGE);

        if (isNull(transactionRecord.getDate())) {
            var minskDate = LocalDateTime.now(MINSK_TIME).toInstant(ZoneOffset.UTC);
            transactionRecord.setDate(Date.from(minskDate));
        }

        return transactionRecordRepository.save(transactionRecord);
    }

    @Override
    public void deleteTransactionRecordById(String transactionRecordId) {
        requireNonNull(transactionRecordId, NULL_TRANSACTION_RECORD_ID_ERROR_MESSAGE);

        if (transactionRecordRepository.findById(transactionRecordId).isEmpty()) {
            throw new IllegalArgumentException(format(NOT_FOUND_TRANSACTION_RECORD_ERROR_MESSAGE, transactionRecordId));
        }

        transactionRecordRepository.deleteById(transactionRecordId);
    }

    @Override
    public void deleteTransactionRecordsByTelegramChatId(String telegramChatId) {
        requireNonNull(telegramChatId, NULL_TELEGRAM_CHAT_ID_ERROR_MESSAGE);

        if (transactionRecordRepository.findAllByTelegramChatId(telegramChatId).isEmpty()) {
            throw new IllegalArgumentException(format(NOT_FOUND_TELEGRAM_CHAT_ID_ERROR_MESSAGE, telegramChatId));
        }

        transactionRecordRepository.deleteByTelegramChatId(telegramChatId);
    }

    @Override
    public TransactionRecordModel updateTransactionRecord(TransactionRecordModel transactionRecord) {
        requireNonNull(transactionRecord, NULL_TRANSACTION_RECORD_ERROR_MESSAGE);

        var existingTransactionRecord = transactionRecordRepository.findById(transactionRecord.getId())
                .orElseThrow(() -> new IllegalArgumentException(format(NOT_FOUND_TRANSACTION_RECORD_ERROR_MESSAGE, transactionRecord.getId())));

        transactionRecordValidFieldsMapper.mapValidFields(transactionRecord, existingTransactionRecord);

        return transactionRecordRepository.save(existingTransactionRecord);
    }
}
