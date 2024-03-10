package com.walty.telegram.web.telegram.command.validator

import com.walty.telegram.web.telegram.command.validator.impl.SaveTransactionRecordCommandValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest
class SaveTransactionRecordCommandValidatorIntegrationTest extends Specification {

    var VALUE_NOT_FOUND_MESSAGE = "No value found, please use /saveTransactionRecord {currency code} {value} (for example 100)"
    var CURRENCY_NOT_FOUND_MESSAGE = "No currency code found, please use /saveTransactionRecord {currency code} (for example USD) {value}"
    var DESCRIPTION_SHOULD_BE_LAST_MESSAGE = "Description starts with -d and should be last in a command"

    @Shared
    var VALID_INPUT_WITH_COMMAND_AND_CURRENCY_AND_VALUE_AND_DESCRIPTION = "/saveTransactionRecord USD 1 -d description"
    @Shared
    var VALID_INPUT_WITH_COMMAND_AND_CURRENCY_AND_VALUE = "/saveTransactionRecord USD 1"
    @Shared
    var VALID_INPUT_WITH_CURRENCY_AND_VALUE = "USD 1"

    @Shared
    var INVALID_INPUT_WITH_COMMAND_AND_CURRENCY_AND_VALUE_AND_DESCRIPTION = "/saveTransactionRecord -d description USD 1"
    @Shared
    var INVALID_INPUT_WITH_COMMAND_AND_INVALID_CURRENCY_AND_VALUE = "/saveTransactionRecord INV 1"
    @Shared
    var INVALID_INPUT_WITH_CURRENCY_AND_VALUE = "INV 1"
    @Shared
    var INVALID_INPUT_WITH_CURRENCY = "USD"

    @Autowired
    SaveTransactionRecordCommandValidator saveTransactionRecordCommandValidator

    @Unroll
    void "should validate #input"() {
        expect:
        var errorOptional = saveTransactionRecordCommandValidator.isValid(input)

        errorOptional.isEmpty()

        where:
        input << [VALID_INPUT_WITH_COMMAND_AND_CURRENCY_AND_VALUE_AND_DESCRIPTION, VALID_INPUT_WITH_COMMAND_AND_CURRENCY_AND_VALUE, VALID_INPUT_WITH_CURRENCY_AND_VALUE]
    }

    void "should invalidate #input given invalid currency"() {
        expect:
        var errorOptional = saveTransactionRecordCommandValidator.isValid(input)

        errorOptional.isPresent()
        CURRENCY_NOT_FOUND_MESSAGE == errorOptional.get()

        where:
        input << [INVALID_INPUT_WITH_COMMAND_AND_INVALID_CURRENCY_AND_VALUE, INVALID_INPUT_WITH_CURRENCY_AND_VALUE]
    }

    void "should invalidate input given invalid description"() {
        when:
        var errorOptional = saveTransactionRecordCommandValidator.isValid(INVALID_INPUT_WITH_COMMAND_AND_CURRENCY_AND_VALUE_AND_DESCRIPTION)

        then:
        errorOptional.isPresent()
        DESCRIPTION_SHOULD_BE_LAST_MESSAGE == errorOptional.get()
    }

    void "should invalidate input given invalid value"() {
        when:
        var errorOptional = saveTransactionRecordCommandValidator.isValid(INVALID_INPUT_WITH_CURRENCY)

        then:
        errorOptional.isPresent()
        VALUE_NOT_FOUND_MESSAGE == errorOptional.get()
    }
}
