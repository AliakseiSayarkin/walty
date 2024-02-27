package com.walty.transaction.facade.impl;

import com.walty.transaction.facade.TransactionRecordFacade;
import com.walty.transaction.facade.converter.TransactionRecordDTOConverter;
import com.walty.transaction.facade.converter.TransactionRecordModelConverter;
import com.walty.transaction.service.TransactionRecordService;
import com.walty.transaction.service.model.TransactionRecordModel;
import com.walty.transaction.web.dto.TransactionRecordDTO;
import com.walty.transaction.web.dto.TransactionSummaryDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Service
@AllArgsConstructor
public class DefaultTransactionRecordFacade implements TransactionRecordFacade {

    private static final String NULL_DATE_ERROR_MESSAGE = "Parameter 'date' cannot be null";
    private static final String NULL_TELEGRAM_CHAT_ID_ERROR_MESSAGE = "Parameter 'telegramChatId' cannot be null";
    private static final String NULL_TRANSACTION_RECORD_ERROR_MESSAGE = "Parameter 'transactionRecord' cannot be null";

    private TransactionRecordService transactionRecordService;
    private TransactionRecordDTOConverter transactionRecordDTOConverter;
    private TransactionRecordModelConverter transactionRecordModelConverter;

    @Override
    public List<TransactionRecordDTO> getCurrentMonthTransactionRecordsByTelegramChatId(String telegramChatId) {
        requireNonNull(telegramChatId, NULL_TELEGRAM_CHAT_ID_ERROR_MESSAGE);

        var currenDate = getStartOfMonth();
        return transactionRecordService.getTransactionRecordsByTelegramChatIdAndDate(telegramChatId, currenDate).stream()
                .map(transactionRecordDTOConverter::convert)
                .toList();
    }

    private Date getStartOfMonth() {
        var calendar = Calendar.getInstance();

        calendar.setTime(calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    @Override
    public List<TransactionRecordDTO> getTransactionRecordsByTelegramChatIdAndDate(String telegramChatId, Date date) {
        requireNonNull(telegramChatId, NULL_TELEGRAM_CHAT_ID_ERROR_MESSAGE);
        requireNonNull(date, NULL_DATE_ERROR_MESSAGE);

        return transactionRecordService.getTransactionRecordsByTelegramChatIdAndDate(telegramChatId, date).stream()
                .map(transactionRecordDTOConverter::convert)
                .toList();
    }

    @Override
    public TransactionSummaryDTO getTransactionSummaryForDateAndTelegramChatId(String telegramChatId, Date date) {
        requireNonNull(telegramChatId, NULL_TELEGRAM_CHAT_ID_ERROR_MESSAGE);
        requireNonNull(date, NULL_DATE_ERROR_MESSAGE);

        return createTransactionSummary(telegramChatId, date);
    }

    private TransactionSummaryDTO createTransactionSummary(String telegramChatId, Date date) {
        var transactionRecords = transactionRecordService.getTransactionRecordsByTelegramChatIdAndDate(telegramChatId, date);

        double usdTotal = 0, eurTotal = 0, bynTotal = 0, rubTotal = 0;
        for (TransactionRecordModel transactionRecord : transactionRecords) {
            usdTotal += transactionRecord.getUsd();
            eurTotal += transactionRecord.getEur();
            bynTotal += transactionRecord.getByn();
            rubTotal += transactionRecord.getRub();
        }

        return TransactionSummaryDTO.builder()
                .telegramChatId(telegramChatId)
                .usd(usdTotal)
                .eur(eurTotal)
                .byn(bynTotal)
                .rub(rubTotal)
                .build();
    }

    @Override
    public TransactionRecordDTO saveTransactionRecord(TransactionRecordDTO transactionRecord) {
        requireNonNull(transactionRecord, NULL_TRANSACTION_RECORD_ERROR_MESSAGE);

        return transactionRecordDTOConverter.convert(
                transactionRecordService.saveTransactionRecord(transactionRecordModelConverter.convert(transactionRecord))
        );
    }

    @Override
    public void deleteTransactionRecordById(String transactionRecordId) {
        requireNonNull(transactionRecordId, NULL_TELEGRAM_CHAT_ID_ERROR_MESSAGE);

        transactionRecordService.deleteTransactionRecordById(transactionRecordId);
    }

    @Override
    public void deleteTransactionRecordsByTelegramChatId(String telegramChatId) {
        requireNonNull(telegramChatId, NULL_TELEGRAM_CHAT_ID_ERROR_MESSAGE);

        transactionRecordService.deleteTransactionRecordsByTelegramChatId(telegramChatId);
    }

    @Override
    public TransactionRecordDTO updateTransactionRecord(TransactionRecordDTO transactionRecord) {
        requireNonNull(transactionRecord, NULL_TRANSACTION_RECORD_ERROR_MESSAGE);

        return transactionRecordDTOConverter.convert(
                transactionRecordService.updateTransactionRecord(transactionRecordModelConverter.convert(transactionRecord))
        );
    }
}
