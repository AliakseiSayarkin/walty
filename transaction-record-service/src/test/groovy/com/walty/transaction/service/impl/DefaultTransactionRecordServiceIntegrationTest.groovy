package com.walty.transaction.service.impl

import com.walty.transaction.service.model.TransactionRecordModel
import com.walty.transaction.web.validator.TransactionRecordValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import spock.lang.Shared
import spock.lang.Specification

import static java.util.Objects.nonNull

@SpringBootTest
class DefaultTransactionRecordServiceIntegrationTest extends Specification {

    @Shared
    var IDS = ["1", "2", "3", "4", "5"]

    var DATE = new Date()
    var DESCRIPTION = "description"
    var USD_VALUE = 100d
    var EUR_VALUE = 200d
    var BYN_VALUE = 300d
    var RUB_VALUE = 400d

    var TELEGRAM_CHAT_ID_TO_SAVE = "6"

    @Autowired
    MongoTemplate mongoTemplate
    @Autowired
    TransactionRecordValidator transactionRecordValidator
    @Autowired
    DefaultTransactionRecordService defaultTransactionRecordService

    void setup() {
        mongoTemplate.dropCollection(TransactionRecordModel.class)

        IDS.forEach { id -> {
            var transactionRecord = TransactionRecordModel.builder()
                    .id(id)
                    .telegramChatId(id)
                    .date(DATE)
                    .description(DESCRIPTION)
                    .usd(USD_VALUE)
                    .eur(EUR_VALUE)
                    .byn(BYN_VALUE)
                    .rub(RUB_VALUE)
                    .build()

            mongoTemplate.save(transactionRecord)
        }}
    }

    void "should return transaction records by #telegramChatId"() {
        when:
        var transactionRecords = defaultTransactionRecordService.getTransactionRecordsByTelegramChatIdAndDate(telegramChatId, DATE)

        then:
        transactionRecords.forEach { transactionRecord ->
            {
                nonNull(transactionRecord.getId())
                telegramChatId == transactionRecord.getId()
                telegramChatId == transactionRecord.getTelegramChatId()
                DATE == transactionRecord.getDate()
                DESCRIPTION == transactionRecord.getDescription()
                USD_VALUE == transactionRecord.getUsd()
                EUR_VALUE == transactionRecord.getEur()
                BYN_VALUE == transactionRecord.getByn()
                RUB_VALUE == transactionRecord.getRub()
            }
        }

        where:
        telegramChatId << IDS
    }

    void "should save transaction record"() {
        given:
        var toSaveTransactionRecord = TransactionRecordModel.builder()
                .telegramChatId(TELEGRAM_CHAT_ID_TO_SAVE)
                .date(DATE)
                .description(DESCRIPTION)
                .usd(USD_VALUE)
                .eur(EUR_VALUE)
                .byn(BYN_VALUE)
                .rub(RUB_VALUE)
                .build()

        when:
        var savedTransactionRecord = defaultTransactionRecordService.saveTransactionRecord(toSaveTransactionRecord)

        then:
        nonNull(savedTransactionRecord)
        nonNull(savedTransactionRecord.getId())
        toSaveTransactionRecord.getTelegramChatId() == savedTransactionRecord.getTelegramChatId()
        toSaveTransactionRecord.getDate() == savedTransactionRecord.getDate()
        toSaveTransactionRecord.getDescription() == savedTransactionRecord.getDescription()
        toSaveTransactionRecord.getUsd() == savedTransactionRecord.getUsd()
        toSaveTransactionRecord.getEur() == savedTransactionRecord.getEur()
        toSaveTransactionRecord.getByn() == savedTransactionRecord.getByn()
        toSaveTransactionRecord.getRub() == savedTransactionRecord.getRub()

        and:
        1 == defaultTransactionRecordService.getTransactionRecordsByTelegramChatIdAndDate(TELEGRAM_CHAT_ID_TO_SAVE, DATE).size()
    }

    void "should delete transaction record by id"() {
        given:
        var idToDelete = IDS.getFirst()

        when:
        defaultTransactionRecordService.deleteTransactionRecordById(idToDelete)

        then:
        noExceptionThrown()
        defaultTransactionRecordService.getTransactionRecordsByTelegramChatIdAndDate(idToDelete, DATE).isEmpty()
    }

    void "should delete transaction records by telegram chat id"() {
        given:
        var telegramChatIdToDelete = IDS.getFirst()

        var toSaveTransactionRecord = TransactionRecordModel.builder()
                .id(TELEGRAM_CHAT_ID_TO_SAVE)
                .telegramChatId(telegramChatIdToDelete)
                .date(DATE)
                .description(DESCRIPTION)
                .usd(USD_VALUE)
                .eur(EUR_VALUE)
                .byn(BYN_VALUE)
                .rub(RUB_VALUE)
                .build()

        mongoTemplate.save(toSaveTransactionRecord)

        when:
        defaultTransactionRecordService.deleteTransactionRecordsByTelegramChatId(telegramChatIdToDelete)

        then:
        noExceptionThrown()
        defaultTransactionRecordService.getTransactionRecordsByTelegramChatIdAndDate(telegramChatIdToDelete, DATE).isEmpty()
    }

    void "should update transaction record"() {
        given:
        var toSaveTransactionRecord = TransactionRecordModel.builder()
                .id(IDS.getFirst())
                .telegramChatId(TELEGRAM_CHAT_ID_TO_SAVE)
                .date(DATE)
                .description(DESCRIPTION)
                .usd(USD_VALUE)
                .eur(EUR_VALUE)
                .byn(BYN_VALUE)
                .rub(RUB_VALUE)
                .build()

        when:
        var savedTransactionRecord = defaultTransactionRecordService.updateTransactionRecord(toSaveTransactionRecord)

        then:
        nonNull(savedTransactionRecord)
        nonNull(savedTransactionRecord.getId())
        toSaveTransactionRecord.getTelegramChatId() == savedTransactionRecord.getTelegramChatId()
        toSaveTransactionRecord.getDate() == savedTransactionRecord.getDate()
        toSaveTransactionRecord.getDescription() == savedTransactionRecord.getDescription()
        toSaveTransactionRecord.getUsd() == savedTransactionRecord.getUsd()
        toSaveTransactionRecord.getEur() == savedTransactionRecord.getEur()
        toSaveTransactionRecord.getByn() == savedTransactionRecord.getByn()
        toSaveTransactionRecord.getRub() == savedTransactionRecord.getRub()

        and:
        1 == defaultTransactionRecordService.getTransactionRecordsByTelegramChatIdAndDate(TELEGRAM_CHAT_ID_TO_SAVE, DATE).size()
    }
}
