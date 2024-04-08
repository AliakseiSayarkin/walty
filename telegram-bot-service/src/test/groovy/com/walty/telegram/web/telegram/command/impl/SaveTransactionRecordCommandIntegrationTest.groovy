package com.walty.telegram.web.telegram.command.impl

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.walty.telegram.service.ParsingService
import com.walty.telegram.web.dto.CurrencyDTO
import com.walty.telegram.web.dto.CurrencyValuesDTO
import com.walty.telegram.web.dto.TransactionRecordDTO
import com.walty.telegram.web.integration.CurrencyExchangeWebClient
import com.walty.telegram.web.integration.TransactionRecordWebClient
import com.walty.telegram.web.telegram.command.validator.CommandValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class SaveTransactionRecordCommandIntegrationTest extends Specification {

    var ERROR_RESPONSE = "Unable to save transaction record please try again later"
    var SUCCESS_RESPONSE = "Transaction record is saved"
    var CURRENCY_NOT_FOUND_MESSAGE = "No currency code found, please use /saveTransactionRecord {currency code} (for example USD) {value}"

    var VALID_COMMAND = "/saveTransactionRecord 10 USD -d description"
    var INVALID_COMMAND = "invalid command"

    var CURRENCY_VALUE = 100d

    var TELEGRAM_CHAT_ID = 1l

    @Autowired
    @Collaborator
    ParsingService parsingService

    @Collaborator
    TransactionRecordWebClient transactionRecordWebClient = Mock()
    @Collaborator
    CurrencyExchangeWebClient currencyExchangeWebClient = Mock()

    @Autowired
    @Collaborator
    CommandValidator saveTransactionRecordCommandValidator

    @Subject
    SaveTransactionRecordCommand saveTransactionRecordCommand

    void setup() {
        given:
        var currencyValues = CurrencyValuesDTO.builder()
                .usd(CURRENCY_VALUE)
                .eur(CURRENCY_VALUE)
                .byn(CURRENCY_VALUE)
                .rub(CURRENCY_VALUE)
                .build()

        when:
        currencyExchangeWebClient.getCurrencyValues(_ as CurrencyDTO) >> currencyValues

        and:
        transactionRecordWebClient.saveTransactionRecord(_ as String, _ as TransactionRecordDTO) >> new TransactionRecordDTO()
    }

    void "should save transaction record given valid command"() {
        when:
        var result = saveTransactionRecordCommand.execute(TELEGRAM_CHAT_ID, VALID_COMMAND)

        then:
        SUCCESS_RESPONSE == result
    }

    void "should return error message given invalid command"() {
        when:
        var result = saveTransactionRecordCommand.execute(TELEGRAM_CHAT_ID, INVALID_COMMAND)

        then:
        CURRENCY_NOT_FOUND_MESSAGE == result
    }

    void "should return error message when transaction record web client returned error"() {
        when:
        var result = saveTransactionRecordCommand.execute(TELEGRAM_CHAT_ID, VALID_COMMAND)

        then:
        ERROR_RESPONSE == result

        and:
        transactionRecordWebClient.saveTransactionRecord(_ as String, _ as TransactionRecordDTO) >> null
    }
}
