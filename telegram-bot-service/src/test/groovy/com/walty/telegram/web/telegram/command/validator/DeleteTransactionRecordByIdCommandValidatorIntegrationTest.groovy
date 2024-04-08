package com.walty.telegram.web.telegram.command.validator

import com.walty.telegram.web.telegram.command.validator.impl.DeleteTransactionRecordByIdCommandValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class DeleteTransactionRecordByIdCommandValidatorIntegrationTest extends Specification {

    var NOT_FOUND_TRANSACTION_RECORD_ID_MESSAGE = "No transaction record id found, please use /deleteTransactionRecordById {transaction record id} (for example 1)"
    var INVALID_TRANSACTION_RECORD_ID_MESSAGE = "Transaction record id must be a single word, please use /deleteTransactionRecordById {transaction record id} (for example 1)"

    var VALID_INPUT_WITH_COMMAND_AND_ID = "/deleteTransactionRecordById 100"
    var INVALID_INPUT_WITH_COMMAND_AND_ID = "/exchange id: 100"
    var INVALID_INPUT = "/exchange"


    @Autowired
    DeleteTransactionRecordByIdCommandValidator deleteTransactionRecordByIdCommandValidator

    void "should validate input"() {
        when:
        var errorOptional = deleteTransactionRecordByIdCommandValidator.isValid(VALID_INPUT_WITH_COMMAND_AND_ID)

        then:
        errorOptional.isEmpty()
    }

    void "should invalidate input given invalid transaction record id"() {
        when:
        var errorOptional = deleteTransactionRecordByIdCommandValidator.isValid(INVALID_INPUT_WITH_COMMAND_AND_ID)

        then:
        errorOptional.isPresent()
        INVALID_TRANSACTION_RECORD_ID_MESSAGE == errorOptional.get()
    }

    void "should invalidate input given empty transaction record id"() {
        when:
        var errorOptional = deleteTransactionRecordByIdCommandValidator.isValid(INVALID_INPUT)

        then:
        errorOptional.isPresent()
        NOT_FOUND_TRANSACTION_RECORD_ID_MESSAGE == errorOptional.get()
    }
}
