package com.walty.transaction.service;

import com.walty.transaction.service.model.TransactionRecordModel;

import java.util.Date;
import java.util.List;

public interface TransactionRecordService {

    List<TransactionRecordModel> getTransactionRecordsByTelegramChatIdAndDate(String telegramChatId, Date date);
    TransactionRecordModel saveTransactionRecord(TransactionRecordModel transactionRecord);
    void deleteTransactionRecordById(String transactionRecordId);
    void deleteTransactionRecordsByTelegramChatId(String telegramChatId);
    TransactionRecordModel updateTransactionRecord(TransactionRecordModel transactionRecord);
}
