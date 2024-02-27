package com.walty.currency.web.api.v1

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.walty.currency.facade.CurrencyExchangeFacade
import com.walty.currency.web.dto.CurrencyDTO
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static com.walty.currency.Currency.USD

@SpringBootTest
class CurrencyExchangeControllerUnitTest extends Specification {

    var CURRENCY_VALUE = 100d

    @Collaborator
    CurrencyExchangeFacade currencyExchangeFacade = Mock()

    @Subject
    CurrencyExchangeController currencyExchangeController

    void "should return 404 Not Found when convertCurrency() given not working integration service"() {
        given:
        var currencyToSell = new CurrencyDTO("BYN", CURRENCY_VALUE)
        currencyExchangeFacade.exchangeCurrency(currencyToSell, USD.toString()) >> Optional.empty()

        when:
        var response = currencyExchangeController.exchangeCurrency(currencyToSell, USD.toString())

        then:
        response.getStatusCode().is4xxClientError()
    }

    void "should return 404 Not Found when createCurrencyValues() given not working integration service"() {
        given:
        var currencyToSell = new CurrencyDTO("BYN", CURRENCY_VALUE)
        currencyExchangeFacade.createCurrencyValues(currencyToSell) >> Optional.empty()

        when:
        var response = currencyExchangeController.createCurrencyValues(currencyToSell)

        then:
        response.getStatusCode().is4xxClientError()
    }
}
