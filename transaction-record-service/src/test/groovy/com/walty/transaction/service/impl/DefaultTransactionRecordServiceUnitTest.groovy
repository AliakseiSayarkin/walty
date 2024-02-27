package com.walty.transaction.service.impl

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.walty.transaction.dao.TransactionRecordRepository
import com.walty.transaction.service.mapper.TransactionRecordValidFieldsMapper
import com.walty.transaction.service.model.TransactionRecordModel
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static java.util.Objects.nonNull

@SpringBootTest
class DefaultTransactionRecordServiceUnitTest extends Specification {

    var TRANSACTION_RECORD_ID_TO_SAVE = "1"
    var TELEGRAM_CHAT_ID_TO_DELETE = "2"
    var TRANSACTION_RECORD_ID_TO_UPDATE = "3"
    var TRANSACTION_RECORD_TELEGRAM_CHAT_ID_TO_GET = "4"

    var EXISTING_IDS = ["1", "2", "3", "4", "5"]

    var EXISTING_DESCRIPTION = "existing description"
    var EXISTING_DATE = new Date()
    var EXISTING_CURRENCY_VALUE = 100d

    var existingTransactionRecords = new ArrayList<>()

    @Collaborator
    TransactionRecordRepository transactionRecordRepository = Mock()
    @Collaborator
    TransactionRecordValidFieldsMapper transactionRecordValidFieldsMapper = GroovyMock()

    @Subject
    DefaultTransactionRecordService defaultTransactionRecordService

    void setup() {
        given:
        EXISTING_IDS.forEach { id ->
            {
                var transactionRecord = TransactionRecordModel.builder()
                        .id(id)
                        .telegramChatId(id)
                        .date(EXISTING_DATE)
                        .description(EXISTING_DESCRIPTION)
                        .usd(EXISTING_CURRENCY_VALUE)
                        .eur(EXISTING_CURRENCY_VALUE)
                        .byn(EXISTING_CURRENCY_VALUE)
                        .rub(EXISTING_CURRENCY_VALUE)
                        .build()

                existingTransactionRecords.add(transactionRecord)
            }
        }

        and:
        var savedTransactionRecord = TransactionRecordModel.builder()
                .id(TRANSACTION_RECORD_ID_TO_SAVE)
                .telegramChatId(TRANSACTION_RECORD_ID_TO_SAVE)
                .date(EXISTING_DATE)
                .description(EXISTING_DESCRIPTION)
                .usd(EXISTING_CURRENCY_VALUE)
                .eur(EXISTING_CURRENCY_VALUE)
                .byn(EXISTING_CURRENCY_VALUE)
                .rub(EXISTING_CURRENCY_VALUE)
                .build()

        transactionRecordRepository.save(_ as TransactionRecordModel) >> savedTransactionRecord

        and:
        transactionRecordRepository.findById(TELEGRAM_CHAT_ID_TO_DELETE) >> Optional.of(savedTransactionRecord)

        and:
        transactionRecordRepository.findById(TRANSACTION_RECORD_ID_TO_UPDATE) >> Optional.of(savedTransactionRecord)

        and:
        transactionRecordValidFieldsMapper.mapValidFields(_ as TransactionRecordModel, _ as TransactionRecordModel) >> {}
    }

    void "should return transaction records by telegram chat id and date"() {
        given:
        transactionRecordRepository.findAllByTelegramChatIdAndDateGreaterThanEqualOrderByDateAsc(TRANSACTION_RECORD_TELEGRAM_CHAT_ID_TO_GET, EXISTING_DATE) >> existingTransactionRecords

        when:
        var foundRecords = defaultTransactionRecordService.getTransactionRecordsByTelegramChatIdAndDate(TRANSACTION_RECORD_TELEGRAM_CHAT_ID_TO_GET, EXISTING_DATE)

        then:
        existingTransactionRecords == foundRecords
    }


    void "should throw NullPointerException when getTransactionRecordsByTelegramChatIdAndDate() given null telegram chat id and date"() {
        when:
        defaultTransactionRecordService.getTransactionRecordsByTelegramChatIdAndDate(null, null)

        then:
        thrown NullPointerException
    }

    void "should save transaction record"() {
        given:
        var transactionRecordToSave = TransactionRecordModel.builder()
                .telegramChatId(TRANSACTION_RECORD_ID_TO_SAVE)
                .date(EXISTING_DATE)
                .description(EXISTING_DESCRIPTION)
                .usd(EXISTING_CURRENCY_VALUE)
                .eur(EXISTING_CURRENCY_VALUE)
                .byn(EXISTING_CURRENCY_VALUE)
                .rub(EXISTING_CURRENCY_VALUE)
                .build()
        when:
        var savedTransactionRecord = defaultTransactionRecordService.saveTransactionRecord(transactionRecordToSave)

        then:
        nonNull(savedTransactionRecord)
        nonNull(savedTransactionRecord.getId())
    }

    void "should throw NullPointerException when saveTransactionRecord() given null transaction record id"() {
        when:
        defaultTransactionRecordService.saveTransactionRecord(null)

        then:
        thrown NullPointerException
    }

    void "should delete transaction record by id"() {
        when:
        defaultTransactionRecordService.deleteTransactionRecordById(TELEGRAM_CHAT_ID_TO_DELETE)

        then:
        noExceptionThrown()
    }

    void "should throw NullPointerException when deleteTransactionRecordById() given null id"() {
        when:
        defaultTransactionRecordService.deleteTransactionRecordById(null)

        then:
        thrown NullPointerException
    }

    void "should throw IllegalArgumentException when deleteTransactionRecordById() given non existing id"() {
        given:
        var nonExistingId = TELEGRAM_CHAT_ID_TO_DELETE + "non existing"

        and:
        transactionRecordRepository.findById(nonExistingId) >> Optional.empty()

        when:
        defaultTransactionRecordService.deleteTransactionRecordById(nonExistingId)

        then:
        thrown IllegalArgumentException
    }

    void "should delete transaction record by transaction record id"() {
        given:
        transactionRecordRepository.findAllByTelegramChatId(TELEGRAM_CHAT_ID_TO_DELETE) >> existingTransactionRecords

        when:
        defaultTransactionRecordService.deleteTransactionRecordsByTelegramChatId(TELEGRAM_CHAT_ID_TO_DELETE)

        then:
        noExceptionThrown()
    }

    void "should throw NullPointerException when deleteTransactionRecordsByTelegramChatId() given null telegram chat id"() {
        when:
        defaultTransactionRecordService.deleteTransactionRecordsByTelegramChatId(null)

        then:
        thrown NullPointerException
    }

    void "should throw IllegalArgumentException when deleteTransactionRecordsByTelegramChatId() given non existing telegram chat id"() {
        given:
        var nonExistingTelegramChatId = TELEGRAM_CHAT_ID_TO_DELETE + "non existing"

        and:
        transactionRecordRepository.findAllByTelegramChatId(nonExistingTelegramChatId) >> Collections.emptyList()

        when:
        defaultTransactionRecordService.deleteTransactionRecordsByTelegramChatId(nonExistingTelegramChatId)

        then:
        thrown IllegalArgumentException
    }

    void "should update transaction record"() {
        given:
        var toUpdateTransactionRecord = TransactionRecordModel.builder()
                .id(TRANSACTION_RECORD_ID_TO_UPDATE)
                .telegramChatId(TRANSACTION_RECORD_ID_TO_UPDATE)
                .date(EXISTING_DATE)
                .description(EXISTING_DESCRIPTION)
                .usd(EXISTING_CURRENCY_VALUE)
                .eur(EXISTING_CURRENCY_VALUE)
                .byn(EXISTING_CURRENCY_VALUE)
                .rub(EXISTING_CURRENCY_VALUE)
                .build()

        when:
        var updatedTransactionRecord = defaultTransactionRecordService.updateTransactionRecord(toUpdateTransactionRecord)

        then:
        nonNull(updatedTransactionRecord)
        TRANSACTION_RECORD_ID_TO_SAVE == updatedTransactionRecord.id
    }

    void "should throw NullPointerException when updateTransactionRecord() given null id"() {
        when:
        defaultTransactionRecordService.updateTransactionRecord(null)

        then:
        thrown NullPointerException
    }

    void "should throw IllegalArgumentException when updateTransactionRecord() given non existing id"() {
        given:
        var toUpdateTransactionRecord = TransactionRecordModel.builder()
                .id(TRANSACTION_RECORD_ID_TO_UPDATE + "non existing")
                .telegramChatId(TRANSACTION_RECORD_ID_TO_UPDATE)
                .date(EXISTING_DATE)
                .description(EXISTING_DESCRIPTION)
                .usd(EXISTING_CURRENCY_VALUE)
                .eur(EXISTING_CURRENCY_VALUE)
                .byn(EXISTING_CURRENCY_VALUE)
                .rub(EXISTING_CURRENCY_VALUE)
                .build()

        when:
        transactionRecordRepository.findById(TRANSACTION_RECORD_ID_TO_UPDATE + "non existing") >> Optional.empty()
        defaultTransactionRecordService.updateTransactionRecord(toUpdateTransactionRecord)

        then:
        thrown IllegalArgumentException
    }
}
