package com.walty.telegram.web.telegram.command.validator

import com.walty.telegram.web.telegram.command.validator.impl.CurrencyExchangeCommandValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest
class CurrencyExchangeCommandValidatorIntegrationTest extends Specification {

    var INVALID_CURRENCY_MESSAGE = "No currency code found, please use /exchange {currency code} (for example USD)"

    @Shared
    var VALID_INPUT_WITH_COMMAND_AND_CURRENCY_AND_VALUE = "/exchange USD 100"
    @Shared
    var VALID_INPUT_WITH_COMMAND_AND_CURRENCY = "/exchange USD"
    @Shared
    var VALID_INPUT_WITH_CURRENCY = "USD"

    @Shared
    var INVALID_INPUT_WITH_COMMAND_AND_VALUE = "/exchange 100"
    @Shared
    var INVALID_INPUT_WITH_COMMAND = "/exchange"
    @Shared
    var INVALID_INPUT_WITH_VALUE = "100"

    @Autowired
    CurrencyExchangeCommandValidator currencyExchangeValidator

    @Unroll
    void "should validate #input"() {
        expect:
        var errorOptional = currencyExchangeValidator.isValid(input)

        errorOptional.isEmpty()

        where:
        input << [VALID_INPUT_WITH_COMMAND_AND_CURRENCY_AND_VALUE, VALID_INPUT_WITH_COMMAND_AND_CURRENCY, VALID_INPUT_WITH_CURRENCY]
    }

    @Unroll
    void "should invalidate #input"() {
        expect:
        var errorOptional = currencyExchangeValidator.isValid(input)

        errorOptional.isPresent()
        INVALID_CURRENCY_MESSAGE == errorOptional.get()

        where:
        input << [INVALID_INPUT_WITH_COMMAND_AND_VALUE, INVALID_INPUT_WITH_COMMAND, INVALID_INPUT_WITH_VALUE]
    }

    void "should throw NullPointerException when isValid() given null input"() {
        when:
        currencyExchangeValidator.isValid(null)

        then:
        thrown NullPointerException
    }
}
