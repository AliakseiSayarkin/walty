package com.walty.telegram.service.impl

import com.walty.telegram.config.Currency
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest
class DefaultParsingServiceUnitTest extends Specification {

    var COMMAND = "command"

    @Shared
    var VALID_FULL_COMMAND = "/command"
    @Shared
    var VALID_COMMAND_WITH_EMPTY_SPACES = "  /command  "
    @Shared
    var VALID_COMMAND_WITH_PARAMETERS = "/command parameter №1, parameter №2"

    @Shared
    var INVALID_COMMAND = "command"
    @Shared
    var INVALID_COMMAND_AFTER_PARAMETERS = "parameter №1, parameter №2 /command"
    @Shared
    var INVALID_EMPTY_COMMAND = "/ "
    @Shared
    var INVALID_EMPTY_INPUT = " "

    var VALUE = 100

    @Shared
    var VALID_VALUE_INPUT = "100"
    @Shared
    var VALID_VALUE_INPUT_WITH_COMMAND = "/command 100"
    @Shared
    var VALID_MULTIPLE_VALUES = "/command 100 200 300"

    var DOUBLE_VALUE = 100.99

    @Shared
    var VALID_DOUBLE_VALUE_INPUT = "100.99"
    @Shared
    var VALID_DOUBLE_VALUE_INPUT_WITH_COMMAND = "/command 100.99"
    @Shared
    var VALID_DOUBLE_MULTIPLE_VALUES = "/command 100.99 200 300"

    var INVALID_EMPTY_VALUE = " "

    var USD = Currency.USD

    @Shared
    var VALID_CURRENCY = "USD"
    @Shared
    var VALID_CURRENCY_LOWER_CASE = "usd"
    @Shared
    var VALID_CURRENCY_WITH_COMMAND = "/command 100 USD"
    @Shared
    var VALID_CURRENCY_WITH_EMPTY_SPACES = " /command 100 USD "

    @Shared
    var INVALID_CURRENCY = "MRK"
    @Shared
    var INVALID_EMPTY_CURRENCY = " "

    var DESCRIPTION = "description"

    @Shared
    var VALID_DESCRIPTION = "-d description"
    @Shared
    var VALID_DESCRIPTION_WITH_COMMAND = "/command -d description"
    @Shared
    var VALID_DESCRIPTION_WITH_CURRENCY = "USD -d description"
    @Shared
    var VALID_DESCRIPTION_WITH_VALUE = "100 -d description"

    @Shared
    var INVALID_DESCRIPTION = "invalid description"
    @Shared
    var INVALID_DESCRIPTION_WITH_COMMAND = "/command description"
    @Shared
    var INVALID_DESCRIPTION_WITH_CURRENCY = "USD description"
    @Shared
    var INVALID_DESCRIPTION_WITH_VALUE = "USD description"

    var ID = "id"

    var VALID_COMMAND_WITH_TRANSACTION_RECORD_ID = "/command id"
    var INVALID_COMMAND_WITH_TRANSACTION_RECORD_ID = "/command_id"

    @Autowired
    DefaultParsingService parsingService

    @Unroll
    void "should return command given valid #input"() {
        expect:
        var commandOptional = parsingService.getCommand(input)

        commandOptional.isPresent()
        COMMAND == commandOptional.get()

        where:
        input << [VALID_FULL_COMMAND, VALID_COMMAND_WITH_EMPTY_SPACES, VALID_COMMAND_WITH_PARAMETERS]
    }

    @Unroll
    void "should return empty command optional given invalid #input"() {
        expect:
        var commandOptional = parsingService.getCommand(input)

        commandOptional.isEmpty()

        where:
        input << [INVALID_COMMAND, INVALID_COMMAND_AFTER_PARAMETERS, INVALID_EMPTY_COMMAND, INVALID_EMPTY_INPUT]
    }

    @Unroll
    void "should return value given valid #input"() {
        expect:
        var valueOptional = parsingService.getValue(input)

        valueOptional.isPresent()
        VALUE == valueOptional.get()

        where:
        input << [VALID_VALUE_INPUT, VALID_VALUE_INPUT_WITH_COMMAND, VALID_MULTIPLE_VALUES]
    }

    @Unroll
    void "should return value with floating point given valid #input"() {
        expect:
        var valueOptional = parsingService.getValue(input)

        valueOptional.isPresent()
        DOUBLE_VALUE == valueOptional.get()

        where:
        input << [VALID_DOUBLE_VALUE_INPUT, VALID_DOUBLE_VALUE_INPUT_WITH_COMMAND, VALID_DOUBLE_MULTIPLE_VALUES]
    }

    void "should return empty value optional given empty input"() {
        when:
        var valueOptional = parsingService.getValue(INVALID_EMPTY_VALUE)

        then:
        valueOptional.isEmpty()
    }

    @Unroll
    void "should return currency given valid #input"() {
        expect:
        var currencyOptional = parsingService.getCurrency(input)

        currencyOptional.isPresent()
        USD == currencyOptional.get()

        where:
        input << [VALID_CURRENCY, VALID_CURRENCY_LOWER_CASE, VALID_CURRENCY_WITH_COMMAND, VALID_CURRENCY_WITH_EMPTY_SPACES]
    }

    @Unroll
    void "should return empty currency optional given invalid #input"() {
        expect:
        var currencyOptional = parsingService.getCurrency(input)

        currencyOptional.isEmpty()

        where:
        input << [INVALID_CURRENCY, INVALID_EMPTY_CURRENCY]
    }

    @Unroll
    void "should return description given valid #input"() {
        expect:
        var descriptionOptional = parsingService.getDescription(input)

        descriptionOptional.isPresent()
        DESCRIPTION == descriptionOptional.get()

        where:
        input << [VALID_DESCRIPTION, VALID_DESCRIPTION_WITH_COMMAND, VALID_DESCRIPTION_WITH_CURRENCY, VALID_DESCRIPTION_WITH_VALUE]
    }

    @Unroll
    void "should return empty description optional given invalid #input"() {
        expect:
        var descriptionOptional = parsingService.getDescription(input)

        descriptionOptional.isEmpty()

        where:
        input << [INVALID_DESCRIPTION, INVALID_DESCRIPTION_WITH_COMMAND, INVALID_DESCRIPTION_WITH_CURRENCY, INVALID_DESCRIPTION_WITH_VALUE]
    }

    @Unroll
    void "should return #descriptionBeginIndex given valid #input"() {
        expect:
        var descriptionBeginIndexOptional = parsingService.getDescriptionBeginIndex(input)

        descriptionBeginIndexOptional.isPresent()
        descriptionBeginIndex == descriptionBeginIndexOptional.get()

        where:
        input << [VALID_DESCRIPTION, VALID_DESCRIPTION_WITH_COMMAND, VALID_DESCRIPTION_WITH_CURRENCY, VALID_DESCRIPTION_WITH_VALUE]
        descriptionBeginIndex << [0, 9, 4, 4]
    }

    @Unroll
    void "should return empty description begin index given invalid #input"() {
        expect:
        var descriptionBeginIndexOptional = parsingService.getDescriptionBeginIndex(input)

        descriptionBeginIndexOptional.isEmpty()

        where:
        input << [INVALID_DESCRIPTION, INVALID_DESCRIPTION_WITH_COMMAND, INVALID_DESCRIPTION_WITH_CURRENCY, INVALID_DESCRIPTION_WITH_VALUE]
    }

    void "should return transaction record id given valid input"() {
        when:
        var transactionRecordIdOptional = parsingService.getTransactionRecordId(VALID_COMMAND_WITH_TRANSACTION_RECORD_ID)

        then:
        transactionRecordIdOptional.isPresent()
        ID == transactionRecordIdOptional.get()
    }

    void "should return empty transaction record id given invalid input"() {
        when:
        var transactionRecordIdOptional = parsingService.getTransactionRecordId(INVALID_COMMAND_WITH_TRANSACTION_RECORD_ID)

        then:
        transactionRecordIdOptional.isEmpty()
    }
}
