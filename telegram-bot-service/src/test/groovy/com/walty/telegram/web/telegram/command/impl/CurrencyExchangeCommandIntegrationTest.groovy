package com.walty.telegram.web.telegram.command.impl

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.walty.telegram.service.ParsingService
import com.walty.telegram.web.telegram.command.aspect.ExceptionHandler
import com.walty.telegram.web.dto.CurrencyDTO
import com.walty.telegram.web.dto.CurrencyValuesDTO
import com.walty.telegram.web.integration.CurrencyExchangeWebClient
import com.walty.telegram.web.telegram.command.impl.CurrencyExchangeCommand
import com.walty.telegram.web.telegram.command.validator.CommandValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class CurrencyExchangeCommandIntegrationTest extends Specification {

    var INVALID_CURRENCY_MESSAGE = "No currency code found, please use /exchange {currency code} (for example USD)"

    var CURRENCY_VALUES_RESPONSE = """
                                      |USD = %s
                                      |EUR = %s
                                      |BYN = %s
                                      |RUB = %s
                                      """.stripMargin().trim()

    var VALID_INPUT = "/exchange USD 100"
    var INVALID_COMMAND = "/exchange 100"

    var TELEGRAM_CHAT_ID = 1

    var USD = 10d
    var EUR = 20d
    var BYN = 30d
    var RUB = 40d

    @Autowired
    @Collaborator
    ParsingService parsingService

    @Autowired
    @Collaborator
    ExceptionHandler exceptionHandler

    @Autowired
    @Collaborator
    CommandValidator currencyExchangeCommandValidator

    @Collaborator
    CurrencyExchangeWebClient currencyExchangeWebClient = Mock()

    @Subject
    CurrencyExchangeCommand currencyExchangeCommand

    void setup() {
        given:
        var currencyValues = CurrencyValuesDTO.builder()
                .usd(USD)
                .eur(EUR)
                .byn(BYN)
                .rub(RUB)
                .build()

        when:
        currencyExchangeWebClient.getCurrencyValues(_ as CurrencyDTO) >> currencyValues
    }

    void "should execute currency exchange command"() {
        given:
        var input = VALID_INPUT

        when:
        var result = currencyExchangeCommand.execute(TELEGRAM_CHAT_ID, input).trim()

        then:
        !result.isEmpty()

        var expectedResult = String.format(CURRENCY_VALUES_RESPONSE, USD, EUR, BYN, RUB)

        expectedResult == result
    }

    void "should return validation message given invalid input"() {
        given:
        var input = INVALID_COMMAND

        when:
        var result = currencyExchangeCommand.execute(TELEGRAM_CHAT_ID, input).trim()

        then:
        !result.isEmpty()

        INVALID_CURRENCY_MESSAGE == result
    }
}
