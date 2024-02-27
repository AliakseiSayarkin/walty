package com.walty.transaction.facade;

import com.walty.transaction.web.dto.TransactionRecordDTO;
import com.walty.transaction.web.dto.TransactionSummaryDTO;

import java.util.Date;
import java.util.List;

public interface TransactionRecordFacade {

    List<TransactionRecordDTO> getCurrentMonthTransactionRecordsByTelegramChatId(String telegramChatId);
    List<TransactionRecordDTO> getTransactionRecordsByTelegramChatIdAndDate(String telegramChatId, Date date);
    TransactionSummaryDTO getTransactionSummaryForDateAndTelegramChatId(String telegramChatId, Date date);
    TransactionRecordDTO saveTransactionRecord(TransactionRecordDTO transactionRecord);
    void deleteTransactionRecordById(String transactionRecordId);
    void deleteTransactionRecordsByTelegramChatId(String telegramChatId);
    TransactionRecordDTO updateTransactionRecord(TransactionRecordDTO transactionRecord);
}
