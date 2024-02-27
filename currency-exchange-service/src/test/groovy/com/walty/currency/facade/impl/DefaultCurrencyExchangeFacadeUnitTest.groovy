package com.walty.currency.facade.impl

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.walty.currency.facade.converter.CurrencyCodeConverter
import com.walty.currency.facade.converter.CurrencyDTOConverter
import com.walty.currency.facade.converter.CurrencyModelConverter
import com.walty.currency.service.CurrencyExchangeService
import com.walty.currency.service.integration.CurrencyExchangeIntegrationService
import com.walty.currency.service.integration.dto.CurrencyExchangeRateDTO
import com.walty.currency.service.model.CurrencyModel
import com.walty.currency.service.util.RoundingUtil
import com.walty.currency.web.dto.CurrencyDTO
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static com.walty.currency.Currency.BYN
import static com.walty.currency.Currency.USD

@SpringBootTest
class DefaultCurrencyExchangeFacadeUnitTest extends Specification {

    var USA_VALUE = "1"
    var EUR_VALUE = "2"
    var BYN_VALUE = "3"
    var RUB_VALUE = "4"

    var USA_EXCHANGED_VALUE = 100d
    var EUR_EXCHANGED_VALUE = 200d
    var BYN_EXCHANGED_VALUE = 300d
    var RUB_EXCHANGED_VALUE = 400d

    var VALUE = 100d
    var EXPECTED_VALUE = 98d

    var CURRENCY_EXCHANGE_RATE = new CurrencyExchangeRateDTO()
    var CURRENCY_DATA_GIVEN = new CurrencyDTO(USD.toString(), VALUE)
    var CURRENCY_MODEL_GIVEN = new CurrencyModel(USD, VALUE)
    var CURRENCY_MODEL_EXPECTED = new CurrencyModel(BYN, EXPECTED_VALUE)
    var CURRENCY_DATA_EXPECTED = new CurrencyDTO(BYN.toString(), EXPECTED_VALUE)

    @Collaborator
    RoundingUtil roundingUtil = Mock()
    @Collaborator
    CurrencyDTOConverter currencyDataConverter = Mock()
    @Collaborator
    CurrencyCodeConverter currencyCodeConverter = Mock()
    @Collaborator
    CurrencyModelConverter currencyModelConverter = Mock()
    @Collaborator
    CurrencyExchangeService currencyExchangeService = Mock()
    @Collaborator
    CurrencyExchangeIntegrationService currencyExchangeIntegrationService = Mock()

    @Subject
    DefaultCurrencyExchangeFacade currencyExchangeFacade

    void setup() {
        given:
        CURRENCY_EXCHANGE_RATE.setCode(BYN.toString())
        CURRENCY_EXCHANGE_RATE.setUsd(USA_VALUE)
        CURRENCY_EXCHANGE_RATE.setEur(EUR_VALUE)
        CURRENCY_EXCHANGE_RATE.setByn(BYN_VALUE)
        CURRENCY_EXCHANGE_RATE.setRub(RUB_VALUE)

        and:
        currencyExchangeIntegrationService.getCurrencyExchangeRate(USD) >> Optional.of(CURRENCY_EXCHANGE_RATE)

        currencyModelConverter.convert(CURRENCY_DATA_GIVEN) >> CURRENCY_MODEL_GIVEN
        currencyCodeConverter.convert(USD.toString()) >> USD
        currencyCodeConverter.convert(BYN.toString()) >> BYN

        and:
        currencyExchangeService.exchangeCurrency(CURRENCY_MODEL_GIVEN, BYN, CURRENCY_EXCHANGE_RATE) >> Optional.of(CURRENCY_MODEL_EXPECTED)
        currencyDataConverter.convert(CURRENCY_MODEL_EXPECTED) >> CURRENCY_DATA_EXPECTED

        and:
        roundingUtil.round(USA_EXCHANGED_VALUE) >> USA_EXCHANGED_VALUE
        roundingUtil.round(EUR_EXCHANGED_VALUE) >> EUR_EXCHANGED_VALUE
        roundingUtil.round(BYN_EXCHANGED_VALUE) >> BYN_EXCHANGED_VALUE
        roundingUtil.round(RUB_EXCHANGED_VALUE) >> RUB_EXCHANGED_VALUE
    }

    void "should exchange BYN to USD"() {
        when:
        var result = currencyExchangeFacade.exchangeCurrency(CURRENCY_DATA_GIVEN, BYN.toString())

        then:
        result.isPresent()
        result.get().getCode() == CURRENCY_DATA_EXPECTED.getCode()
        result.get().getValue() == CURRENCY_DATA_EXPECTED.getValue()
    }

    void "should return empty currency given null currency to buy code"() {
        when:
        var result = currencyExchangeFacade.exchangeCurrency(CURRENCY_DATA_GIVEN, null)

        then:
        !result.isPresent()
    }

    void "should return empty currency given null currency to sell"() {
        when:
        var result = currencyExchangeFacade.exchangeCurrency(null, BYN.toString())

        then:
        !result.isPresent()
    }

    void "should return empty currency when integration returned empty currency exchange"() {
        when:
        var result = currencyExchangeFacade.exchangeCurrency(CURRENCY_DATA_GIVEN, BYN.toString())

        then:
        currencyExchangeIntegrationService.getCurrencyExchangeRate(USD) >> Optional.empty()
        !result.isPresent()
    }

    void "should create currency values given valid currency to sell"() {
        when:
        var result = currencyExchangeFacade.createCurrencyValues(CURRENCY_DATA_GIVEN)

        then:
        result.isPresent()

        result.get().getUsd() == USA_EXCHANGED_VALUE
        result.get().getEur() == EUR_EXCHANGED_VALUE
        result.get().getByn() == BYN_EXCHANGED_VALUE
        result.get().getRub() == RUB_EXCHANGED_VALUE
    }

    void "should return empty currency values given null currency"() {
        when:
        var result = currencyExchangeFacade.createCurrencyValues(null)

        then:
        !result.isPresent()
    }

    void "should return empty currency values when integration returned empty currency exchange"() {
        when:
        var result = currencyExchangeFacade.createCurrencyValues(CURRENCY_DATA_GIVEN)

        then:
        currencyExchangeIntegrationService.getCurrencyExchangeRate(USD) >> Optional.empty()
        !result.isPresent()
    }
}
