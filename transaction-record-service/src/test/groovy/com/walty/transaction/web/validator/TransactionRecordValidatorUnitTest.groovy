package com.walty.transaction.web.validator

import com.walty.transaction.web.dto.TransactionRecordDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class TransactionRecordValidatorUnitTest extends Specification {

    var ID = "1"
    var TELEGRAM_CHAT_ID = "2"
    var DATE = new Date()
    var DESCRIPTION = "description"
    var CURRENCY_VALUE = 100d
    var INVALID_CURRENCY_VALUE = -1

    @Autowired
    TransactionRecordValidator transactionRecordValidator

    void "should validate for save"() {
        given:
        var validForSaveTransactionRecord = TransactionRecordDTO.builder()
                .telegramChatId(TELEGRAM_CHAT_ID)
                .date(DATE)
                .description(DESCRIPTION)
                .usd(CURRENCY_VALUE)
                .eur(CURRENCY_VALUE)
                .byn(CURRENCY_VALUE)
                .rub(CURRENCY_VALUE)
                .build()

        when:
        transactionRecordValidator.validateForSave(validForSaveTransactionRecord)

        then:
        noExceptionThrown()
    }

    void "should throw NullPointerException when validateForSave() given null"() {
        when:
        transactionRecordValidator.validateForSave(null)

        then:
        thrown NullPointerException
    }

    void "should throw IllegalArgumentException when validateForSave() given non empty id field"() {
        given:
        var validForSaveTransactionRecord = TransactionRecordDTO.builder()
                .id(ID)
                .telegramChatId(TELEGRAM_CHAT_ID)
                .date(DATE)
                .description(DESCRIPTION)
                .usd(CURRENCY_VALUE)
                .eur(CURRENCY_VALUE)
                .byn(CURRENCY_VALUE)
                .rub(CURRENCY_VALUE)
                .build()

        when:
        transactionRecordValidator.validateForSave(validForSaveTransactionRecord)

        then:
        thrown IllegalArgumentException
    }

    void "should throw IllegalArgumentException when validateForSave() given null telegram chat id field"() {
        given:
        var validForSaveTransactionRecord = TransactionRecordDTO.builder()
                .date(DATE)
                .description(DESCRIPTION)
                .usd(CURRENCY_VALUE)
                .eur(CURRENCY_VALUE)
                .byn(CURRENCY_VALUE)
                .rub(CURRENCY_VALUE)
                .build()

        when:
        transactionRecordValidator.validateForSave(validForSaveTransactionRecord)

        then:
        thrown IllegalArgumentException
    }

    void "should throw IllegalArgumentException when validateForSave() given invalid usd field"() {
        given:
        var validForSaveTransactionRecord = TransactionRecordDTO.builder()
                .telegramChatId(TELEGRAM_CHAT_ID)
                .description(DESCRIPTION)
                .usd(INVALID_CURRENCY_VALUE)
                .eur(CURRENCY_VALUE)
                .byn(CURRENCY_VALUE)
                .rub(CURRENCY_VALUE)
                .build()

        when:
        transactionRecordValidator.validateForSave(validForSaveTransactionRecord)

        then:
        thrown IllegalArgumentException
    }

    void "should throw IllegalArgumentException when validateForSave() given invalid eur field"() {
        given:
        var validForSaveTransactionRecord = TransactionRecordDTO.builder()
                .telegramChatId(TELEGRAM_CHAT_ID)
                .description(DESCRIPTION)
                .usd(CURRENCY_VALUE)
                .eur(INVALID_CURRENCY_VALUE)
                .byn(CURRENCY_VALUE)
                .rub(CURRENCY_VALUE)
                .build()

        when:
        transactionRecordValidator.validateForSave(validForSaveTransactionRecord)

        then:
        thrown IllegalArgumentException
    }

    void "should throw IllegalArgumentException when validateForSave() given invalid byn field"() {
        given:
        var validForSaveTransactionRecord = TransactionRecordDTO.builder()
                .telegramChatId(TELEGRAM_CHAT_ID)
                .description(DESCRIPTION)
                .usd(CURRENCY_VALUE)
                .eur(CURRENCY_VALUE)
                .byn(INVALID_CURRENCY_VALUE)
                .rub(CURRENCY_VALUE)
                .build()

        when:
        transactionRecordValidator.validateForSave(validForSaveTransactionRecord)

        then:
        thrown IllegalArgumentException
    }

    void "should throw IllegalArgumentException when validateForSave() given invalid rub field"() {
        given:
        var validForSaveTransactionRecord = TransactionRecordDTO.builder()
                .telegramChatId(TELEGRAM_CHAT_ID)
                .description(DESCRIPTION)
                .usd(CURRENCY_VALUE)
                .eur(CURRENCY_VALUE)
                .byn(CURRENCY_VALUE)
                .rub(INVALID_CURRENCY_VALUE)
                .build()

        when:
        transactionRecordValidator.validateForSave(validForSaveTransactionRecord)

        then:
        thrown IllegalArgumentException
    }

    void "should validate for update"() {
        given:
        var validForSaveTransactionRecord = TransactionRecordDTO.builder()
                .id(ID)
                .telegramChatId(TELEGRAM_CHAT_ID)
                .date(DATE)
                .description(DESCRIPTION)
                .usd(CURRENCY_VALUE)
                .eur(CURRENCY_VALUE)
                .byn(CURRENCY_VALUE)
                .rub(CURRENCY_VALUE)
                .build()

        when:
        transactionRecordValidator.validateForUpdate(validForSaveTransactionRecord)

        then:
        noExceptionThrown()
    }

    void "should throw NullPointerException when validateForUpdate() given null transaction id"() {
        when:
        transactionRecordValidator.validateForUpdate(null)

        then:
        thrown NullPointerException
    }

    void "should throw IllegalArgumentException when validateForUpdate() given null id"() {
        given:
        var validForSaveTransactionRecord = TransactionRecordDTO.builder()
                .telegramChatId(TELEGRAM_CHAT_ID)
                .description(DESCRIPTION)
                .usd(CURRENCY_VALUE)
                .eur(CURRENCY_VALUE)
                .byn(CURRENCY_VALUE)
                .rub(INVALID_CURRENCY_VALUE)
                .build()

        when:
        transactionRecordValidator.validateForSave(validForSaveTransactionRecord)

        then:
        thrown IllegalArgumentException
    }

    void "should validate id"() {
        when:
        transactionRecordValidator.validateTransactionRecordId(ID)

        then:
        noExceptionThrown()
    }

    void "should throw IllegalArgumentException when validateTransactionRecordId() given empty id"() {
        when:
        transactionRecordValidator.validateTransactionRecordId("")

        then:
        thrown IllegalArgumentException
    }

    void "should validate telegram chat id"() {
        when:
        transactionRecordValidator.validateTelegramChatId(ID)

        then:
        noExceptionThrown()
    }

    void "should throw IllegalArgumentException when validateTelegramChatId() given empty telegram chat id"() {
        when:
        transactionRecordValidator.validateTelegramChatId("")

        then:
        thrown IllegalArgumentException
    }
}
