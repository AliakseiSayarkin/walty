package com.walty.transaction.dao

import com.walty.transaction.service.model.TransactionRecordModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static java.util.Objects.nonNull

@SpringBootTest
class TransactionRecordRepositoryIntegrationTest extends Specification {

    @Shared
    var IDS = ["1", "2", "3", "4", "5"]

    var DATE = new Date()
    var DESCRIPTION = "description"
    var USD_VALUE = 100d
    var EUR_VALUE = 200d
    var BYN_VALUE = 300d
    var RUB_VALUE = 400d

    @Autowired
    MongoTemplate mongoTemplate
    @Autowired
    TransactionRecordRepository transactionRecordRepository

    void setup() {
        mongoTemplate.dropCollection(TransactionRecordModel.class)

        IDS.forEach { id ->
            {
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
            }
        }
    }

    void "should return all saved transaction records"() {
        expect:
        transactionRecordRepository.findAll().size() == IDS.size()
    }

    @Unroll
    void "should return transaction records given its #telegramChatId and date"() {
        when:
        var transactionRecords = transactionRecordRepository.findAllByTelegramChatIdAndDateGreaterThanEqualOrderByDateAsc(telegramChatId, DATE)

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

    @Unroll
    void "should return transaction records given #telegramChatId"() {
        when:
        var transactionRecords = transactionRecordRepository.findAllByTelegramChatId(telegramChatId)

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

    void "should delete all transaction records"() {
        when:
        transactionRecordRepository.deleteAll()

        then:
        transactionRecordRepository.findAll().isEmpty()
    }

    @Unroll
    void "should update transaction record currency given #id and date"() {
        when:
        var transactionRecord = transactionRecordRepository.findAllByTelegramChatIdAndDateGreaterThanEqualOrderByDateAsc(id, DATE).getFirst()
        transactionRecord.setUsd(BYN_VALUE + USD_VALUE)
        transactionRecordRepository.save(transactionRecord)

        then:
        var foundTransactionRecord = transactionRecordRepository.findAllByTelegramChatIdAndDateGreaterThanEqualOrderByDateAsc(id, DATE).getFirst()

        nonNull(foundTransactionRecord)
        BYN_VALUE + USD_VALUE == foundTransactionRecord.getUsd()

        where:
        id << IDS
    }

    void "should delete transactions record given telegram chat id"() {
        when:
        transactionRecordRepository.deleteByTelegramChatId(IDS.getFirst())

        then:
        IDS.size() - 1 == transactionRecordRepository.findAll().size()
    }
}
