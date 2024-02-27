package com.walty.transaction.web.api.v1

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.walty.transaction.facade.TransactionRecordFacade
import com.walty.transaction.web.dto.TransactionRecordDTO
import com.walty.transaction.web.dto.TransactionSummaryDTO
import com.walty.transaction.web.validator.DateValidator
import com.walty.transaction.web.validator.TransactionRecordValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class TransactionRecordControllerUnitTest extends Specification {

    var VALID_ID = "1"
    var VALID_TELEGRAM_CHAT_ID = "1"
    var VALID_DATE = new Date()
    var INVALID_DATE = getOneHourInTheFuture()
    var CURRENCY_VALUE = 100d

    TransactionRecordDTO transactionRecord

    @Autowired
    @Collaborator
    DateValidator dateValidator

    @Autowired
    @Collaborator
    TransactionRecordValidator transactionRecordValidator

    @Collaborator
    TransactionRecordFacade transactionRecordFacade = Mock()

    @Subject
    TransactionRecordController transactionRecordController

    void setup() {
        given:
        transactionRecord = TransactionRecordDTO.builder()
                .id(VALID_ID)
                .telegramChatId(VALID_TELEGRAM_CHAT_ID)
                .date(VALID_DATE)
                .build()

        when:
        transactionRecordFacade.getCurrentMonthTransactionRecordsByTelegramChatId(VALID_TELEGRAM_CHAT_ID) >> List.of(transactionRecord)

        and:
        transactionRecordFacade.getTransactionRecordsByTelegramChatIdAndDate(VALID_TELEGRAM_CHAT_ID, VALID_DATE) >> List.of(transactionRecord)
    }

    void "should return current month transaction records given valid telegram chat id"() {
        when:
        var transactionRecords = transactionRecordController.getCurrentMonthTransactionRecordsByTelegramChatId(VALID_TELEGRAM_CHAT_ID)

        then:
        1 == transactionRecords.getBody().size()
        transactionRecord == transactionRecords.getBody().getFirst()
    }

    void "should return transaction records given valid telegram chat id and date"() {
        when:
        var transactionRecords = transactionRecordController.getTransactionRecordsByTelegramChatIdAndDate(VALID_TELEGRAM_CHAT_ID, VALID_DATE)

        then:
        1 == transactionRecords.getBody().size()
        transactionRecord == transactionRecords.getBody().getFirst()
    }

    void "should throw IllegalArgumentException when getTransactionRecordsByTelegramChatIdAndDate() given invalid year"() {
        when:
        transactionRecordController.getTransactionRecordsByTelegramChatIdAndDate(VALID_TELEGRAM_CHAT_ID, INVALID_DATE)

        then:
        thrown IllegalArgumentException
    }

    void "should return transaction summary"() {
        given:
        var transactionSummary = TransactionSummaryDTO.builder()
                .telegramChatId(VALID_TELEGRAM_CHAT_ID)
                .build()

        and:
        transactionRecordFacade.getTransactionSummaryForDateAndTelegramChatId(VALID_TELEGRAM_CHAT_ID, VALID_DATE) >> transactionSummary

        when:
        var returnedTransactionSummary = transactionRecordController.getTransactionSummaryForDateAndTelegramChatId(VALID_TELEGRAM_CHAT_ID, VALID_DATE)

        then:
        transactionSummary == returnedTransactionSummary.getBody()
    }

    void "should throw IllegalArgumentException when getTransactionSummaryForDateAndTelegramChatId() given invalid year"() {
        when:
        transactionRecordController.getTransactionSummaryForDateAndTelegramChatId(VALID_TELEGRAM_CHAT_ID, INVALID_DATE)

        then:
        thrown IllegalArgumentException
    }

    void "should save transaction record"() {
        given:
        var validTransactionRecord = TransactionRecordDTO.builder()
                .telegramChatId(VALID_TELEGRAM_CHAT_ID)
                .date(VALID_DATE)
                .usd(CURRENCY_VALUE)
                .eur(CURRENCY_VALUE)
                .byn(CURRENCY_VALUE)
                .rub(CURRENCY_VALUE)
                .build()

        and:
        transactionRecordFacade.saveTransactionRecord(validTransactionRecord) >> transactionRecord

        when:
        var savedTransactionRecord = transactionRecordController.saveTransactionRecord(validTransactionRecord)

        then:
        transactionRecord == savedTransactionRecord.getBody()
    }

    void "should throw IllegalArgumentException when saveTransactionRecord() given invalid transaction record"() {
        given:
        var validTransactionRecord = TransactionRecordDTO.builder()
                .telegramChatId(VALID_TELEGRAM_CHAT_ID)
                .date(VALID_DATE)
                .build()

        when:
        transactionRecordController.saveTransactionRecord(validTransactionRecord)

        then:
        thrown IllegalArgumentException
    }

    void "should delete transaction record by id"() {
        given:
        transactionRecordFacade.deleteTransactionRecordById(VALID_ID) >> {}

        when:
        transactionRecordController.deleteTransactionRecordById(VALID_ID)

        then:
        noExceptionThrown()
    }

    void "should delete transaction record by telegram chat id"() {
        given:
        transactionRecordFacade.deleteTransactionRecordsByTelegramChatId(VALID_TELEGRAM_CHAT_ID) >> {}

        when:
        transactionRecordController.deleteTransactionRecordById(VALID_TELEGRAM_CHAT_ID)

        then:
        noExceptionThrown()
    }

    void "should update transaction record"() {
        given:
        var validTransactionRecord = TransactionRecordDTO.builder()
                .id(VALID_ID)
                .telegramChatId(VALID_TELEGRAM_CHAT_ID)
                .date(VALID_DATE)
                .usd(CURRENCY_VALUE)
                .eur(CURRENCY_VALUE)
                .byn(CURRENCY_VALUE)
                .rub(CURRENCY_VALUE)
                .build()

        and:
        transactionRecordFacade.updateTransactionRecord(validTransactionRecord) >> transactionRecord

        when:
        var updatedTransactionRecord = transactionRecordController.updateTransactionRecord(validTransactionRecord)

        then:
        transactionRecord == updatedTransactionRecord.getBody()
    }

    void "should throw IllegalArgumentException when updateTransactionRecord() given invalid transaction record"() {
        given:
        var invalidTransactionRecord = TransactionRecordDTO.builder()
                .telegramChatId(VALID_TELEGRAM_CHAT_ID)
                .date(VALID_DATE)
                .build()

        when:
        transactionRecordController.updateTransactionRecord(invalidTransactionRecord)

        then:
        thrown IllegalArgumentException
    }

    Date getOneHourInTheFuture() {
        var calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, 1)
        return calendar.getTime()
    }
}
