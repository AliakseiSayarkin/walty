package com.walty.telegram.web.telegram.command.aspect

import com.walty.telegram.web.telegram.command.impl.CurrencyExchangeCommand
import com.walty.telegram.web.dto.CurrencyDTO
import com.walty.telegram.web.integration.CurrencyExchangeWebClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class ExceptionHandlerIntegrationTest extends Specification {

    var EXCEPTION_MESSAGE_RESPONSE = "Something went wrong please try again"
    var VALID_INPUT = "/exchange USD 100"

    var TELEGRAM_CHAT_ID = 1

    @Autowired
    CurrencyExchangeCommand currencyExchangeCommand

    void "should return default error message when error occurs"() {
        given:
        var input = VALID_INPUT

        and:
        var currencyExchangeIntegrationService = Mock(CurrencyExchangeWebClient)
        currencyExchangeCommand.setCurrencyExchangeIntegrationService(currencyExchangeIntegrationService)

        when:
        var result = currencyExchangeCommand.execute(TELEGRAM_CHAT_ID, input).trim()

        then:
        currencyExchangeIntegrationService.getCurrencyValues(_ as CurrencyDTO) >> { throw new Exception() }

        and:
        !result.isEmpty()

        EXCEPTION_MESSAGE_RESPONSE == result
    }
}
