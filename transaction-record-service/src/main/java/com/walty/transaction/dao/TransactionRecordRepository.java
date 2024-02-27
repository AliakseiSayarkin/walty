package com.walty.transaction.dao;

import com.walty.transaction.service.model.TransactionRecordModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRecordRepository extends MongoRepository<TransactionRecordModel, String> {

    List<TransactionRecordModel> findAllByTelegramChatIdAndDateGreaterThanEqualOrderByDateAsc(String telegramChatId, Date date);
    List<TransactionRecordModel> findAllByTelegramChatId(String telegramChatId);
    void deleteByTelegramChatId(String telegramChatId);
}
