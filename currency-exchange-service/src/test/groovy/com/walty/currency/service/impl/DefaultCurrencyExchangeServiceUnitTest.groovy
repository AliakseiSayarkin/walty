package com.walty.currency.service.impl

import com.walty.currency.service.integration.dto.CurrencyExchangeRateDTO
import com.walty.currency.service.model.CurrencyModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static com.walty.currency.Currency.*

@SpringBootTest
class DefaultCurrencyExchangeServiceUnitTest extends Specification {

    var CURRENCY_VALUE = 100

    var CONVERSION_PERCENTAGE_ADJUSTMENT = 0.98d

    @Shared
    var USD_EXCHANGE_RATE = "1"
    @Shared
    var EUR_EXCHANGE_RATE = "2"
    @Shared
    var BYN_EXCHANGE_RATE = "3"
    @Shared
    var RUB_EXCHANGE_RATE = "4"

    @Autowired
    DefaultCurrencyExchangeService currencyExchangeService

    @Unroll
    void "should exchange #currencyToSellCode to #currencyToBuy given #exchangeTate"() {
        given:
        var currencyToSell = new CurrencyModel(currencyToSellCode, CURRENCY_VALUE)
        var currencyToBuyCode = currencyToBuy
        var currencyExchangeRate = createCurrencyExchangeRate()

        expect:
        var boughtCurrencyOptional = currencyExchangeService.exchangeCurrency(currencyToSell, currencyToBuyCode, currencyExchangeRate)

        boughtCurrencyOptional.isPresent()
        CURRENCY_VALUE * Double.parseDouble(exchangeTate) * CONVERSION_PERCENTAGE_ADJUSTMENT == boughtCurrencyOptional.get().getValue()

        where:
        currencyToBuy << [USD, EUR, BYN, RUB]
        currencyToSellCode << [EUR, BYN, RUB, USD]
        exchangeTate << [USD_EXCHANGE_RATE, EUR_EXCHANGE_RATE, BYN_EXCHANGE_RATE, RUB_EXCHANGE_RATE]
    }

    void "should return empty bought currency given null currency to sell"() {
        given:
        var currencyToSell = null
        var currencyToBuyCode = USD
        var currencyExchangeRate = createCurrencyExchangeRate()

        when:
        var boughtCurrencyOptional = currencyExchangeService.exchangeCurrency(currencyToSell, currencyToBuyCode, currencyExchangeRate)

        then:
        !boughtCurrencyOptional.isPresent()
    }

    void "should return empty bought currency given null currency to buy code"() {
        given:
        var currencyToSell = new CurrencyModel(BYN, CURRENCY_VALUE)
        var currencyToBuyCode = null
        var currencyExchangeRate = createCurrencyExchangeRate()

        when:
        var boughtCurrencyOptional = currencyExchangeService.exchangeCurrency(currencyToSell, currencyToBuyCode, currencyExchangeRate)

        then:
        !boughtCurrencyOptional.isPresent()
    }

    void "should return empty bought currency given null currency exchange rate"() {
        given:
        var currencyToSell = new CurrencyModel(BYN, CURRENCY_VALUE)
        var currencyToBuyCode = USD
        var currencyExchangeRate = null

        when:
        var boughtCurrencyOptional = currencyExchangeService.exchangeCurrency(currencyToSell, currencyToBuyCode, currencyExchangeRate)

        then:
        !boughtCurrencyOptional.isPresent()
    }

    CurrencyExchangeRateDTO createCurrencyExchangeRate() {
        var currencyExchangeRate = new CurrencyExchangeRateDTO()
        currencyExchangeRate.setUsd(USD_EXCHANGE_RATE)
        currencyExchangeRate.setEur(EUR_EXCHANGE_RATE)
        currencyExchangeRate.setByn(BYN_EXCHANGE_RATE)
        currencyExchangeRate.setRub(RUB_EXCHANGE_RATE)

        return currencyExchangeRate
    }
}
