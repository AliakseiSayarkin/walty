package com.walty.transaction.facade.impl

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.walty.transaction.facade.converter.TransactionRecordDTOConverter
import com.walty.transaction.facade.converter.TransactionRecordModelConverter
import com.walty.transaction.service.TransactionRecordService
import com.walty.transaction.service.model.TransactionRecordModel
import com.walty.transaction.web.dto.TransactionRecordDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static java.util.Objects.nonNull

@SpringBootTest
class DefaultTransactionRecordFacadeUnitTest extends Specification {

    var ID = "1"
    var TELEGRAM_CHAT_ID = "1"
    var DATE = new Date()
    var DESCRIPTION = "description"
    var USD_VALUE = 100d
    var EUR_VALUE = 200d
    var BYN_VALUE = 300d
    var RUB_VALUE = 400d

    TransactionRecordModel transactionRecordModel
    TransactionRecordDTO transactionRecordDTO

    @Collaborator
    TransactionRecordService transactionRecordService = Mock()

    @Autowired
    @Collaborator
    TransactionRecordDTOConverter transactionRecordConverter

    @Autowired
    @Collaborator
    TransactionRecordModelConverter transactionRecordModelConverter

    @Subject
    DefaultTransactionRecordFacade defaultTransactionRecordFacade

    void setup() {
        given:
        transactionRecordModel = TransactionRecordModel.builder()
                .id(ID)
                .telegramChatId(TELEGRAM_CHAT_ID)
                .date(DATE)
                .description(DESCRIPTION)
                .usd(USD_VALUE)
                .eur(EUR_VALUE)
                .byn(BYN_VALUE)
                .rub(RUB_VALUE)
                .build()

        transactionRecordDTO = TransactionRecordDTO.builder()
                .id(ID)
                .telegramChatId(TELEGRAM_CHAT_ID)
                .date(DATE)
                .description(DESCRIPTION)
                .usd(USD_VALUE)
                .eur(EUR_VALUE)
                .byn(BYN_VALUE)
                .rub(RUB_VALUE)
                .build()

        and:
        transactionRecordService.getTransactionRecordsByTelegramChatIdAndDate(TELEGRAM_CHAT_ID, _ as Date) >> List.of(transactionRecordModel)
    }

    void "should return transaction records for this month given telegram chat id"() {
        when:
        var transactionRecords = defaultTransactionRecordFacade.getCurrentMonthTransactionRecordsByTelegramChatId(TELEGRAM_CHAT_ID)

        then:
        1 == transactionRecords.size()
        ID == transactionRecords.getFirst().getId()
    }

    void "should throw NullPointerException when getCurrentMonthTransactionRecordsByTelegramChatId() given null telegram chat id"() {
        when:
        defaultTransactionRecordFacade.getCurrentMonthTransactionRecordsByTelegramChatId(null)

        then:
        thrown NullPointerException
    }

    void "should return transaction records given telegram chat id and date"() {
        when:
        var transactionRecords = defaultTransactionRecordFacade.getTransactionRecordsByTelegramChatIdAndDate(TELEGRAM_CHAT_ID, DATE)

        then:
        1 == transactionRecords.size()
        ID == transactionRecords.getFirst().getId()
    }

    void "should throw NullPointerException when getTransactionRecordsByTelegramChatIdAndDate() given null telegram chat id and date"() {
        when:
        defaultTransactionRecordFacade.getTransactionRecordsByTelegramChatIdAndDate(null, null)

        then:
        thrown NullPointerException
    }

    void "should return transaction summary"() {
        when:
        var transactionSummary = defaultTransactionRecordFacade.getTransactionSummaryForDateAndTelegramChatId(TELEGRAM_CHAT_ID, DATE)

        then:
        nonNull(transactionSummary)
        TELEGRAM_CHAT_ID == transactionSummary.getTelegramChatId()
        transactionRecordModel.getUsd() == transactionSummary.getUsd()
        transactionRecordModel.getEur() == transactionSummary.getEur()
        transactionRecordModel.getByn() == transactionSummary.getByn()
        transactionRecordModel.getRub() == transactionSummary.getRub()
    }

    void "should throw NullPointerException when getTransactionSummaryForDateAndTelegramChatId() given null telegram chat id and date"() {
        when:
        defaultTransactionRecordFacade.getTransactionSummaryForDateAndTelegramChatId(null, null)

        then:
        thrown NullPointerException
    }

    void "should save transaction record"() {
        given:
        transactionRecordModel.setId(null)

        and:
        transactionRecordService.saveTransactionRecord(_ as TransactionRecordModel) >> transactionRecordModel

        when:
        var savedTransaction = defaultTransactionRecordFacade.saveTransactionRecord(transactionRecordDTO)

        then:
        nonNull(savedTransaction)
        transactionRecordDTO.getTelegramChatId() == savedTransaction.getTelegramChatId()
        transactionRecordDTO.getDate() == savedTransaction.getDate()
        transactionRecordDTO.getDescription() == savedTransaction.getDescription()
        transactionRecordDTO.getUsd() == savedTransaction.getUsd()
        transactionRecordDTO.getEur() == savedTransaction.getEur()
        transactionRecordDTO.getByn() == savedTransaction.getByn()
        transactionRecordDTO.getRub() == savedTransaction.getRub()
    }

    void "should throw NullPointerException when saveTransactionRecord() given null transaction record"() {
        when:
        defaultTransactionRecordFacade.saveTransactionRecord(null)

        then:
        thrown NullPointerException
    }

    void "should delete transaction record by id"() {
        when:
        defaultTransactionRecordFacade.deleteTransactionRecordById(ID)

        then:
        noExceptionThrown()
    }

    void "should throw NullPointerException when deleteTransactionRecordById() given null id"() {
        when:
        defaultTransactionRecordFacade.deleteTransactionRecordById(null)

        then:
        thrown NullPointerException
    }

    void "should delete transaction record by telegram chat id"() {
        when:
        defaultTransactionRecordFacade.deleteTransactionRecordsByTelegramChatId(TELEGRAM_CHAT_ID)

        then:
        noExceptionThrown()
    }

    void "should throw NullPointerException when deleteTransactionRecordsByTelegramChatId() given null telegram chat id"() {
        when:
        defaultTransactionRecordFacade.deleteTransactionRecordsByTelegramChatId(null)

        then:
        thrown NullPointerException
    }

    void "should update transaction record"() {
        given:
        transactionRecordService.updateTransactionRecord(_ as TransactionRecordModel) >> transactionRecordModel

        when:
        var updatedTransactionRecord = defaultTransactionRecordFacade.updateTransactionRecord(transactionRecordDTO)

        then:
        nonNull(updatedTransactionRecord)
        transactionRecordDTO.getTelegramChatId() == updatedTransactionRecord.getTelegramChatId()
        transactionRecordDTO.getDate() == updatedTransactionRecord.getDate()
        transactionRecordDTO.getDescription() == updatedTransactionRecord.getDescription()
        transactionRecordDTO.getUsd() == updatedTransactionRecord.getUsd()
        transactionRecordDTO.getEur() == updatedTransactionRecord.getEur()
        transactionRecordDTO.getByn() == updatedTransactionRecord.getByn()
        transactionRecordDTO.getRub() == updatedTransactionRecord.getRub()
    }

    void "should throw NullPointerException when updateTransactionRecord() given null transaction record"() {
        when:
        defaultTransactionRecordFacade.updateTransactionRecord(null)

        then:
        thrown NullPointerException
    }
}
