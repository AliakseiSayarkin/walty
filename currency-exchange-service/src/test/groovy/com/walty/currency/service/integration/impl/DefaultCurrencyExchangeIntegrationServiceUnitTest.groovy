package com.walty.currency.service.integration.impl

import com.walty.currency.config.Currency
import com.walty.currency.service.integration.dto.CurrencyExchangeRateDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import spock.lang.Specification

import static com.walty.currency.config.Currency.*
import static io.netty.util.internal.StringUtil.EMPTY_STRING

@SpringBootTest
class DefaultCurrencyExchangeIntegrationServiceUnitTest extends Specification {

    var UNSUCCESSFUL_API_CALL_RESPONSE = """{ "result" : "unsuccessful" }"""
    var UNKNOWN_API_CALL_RESPONSE = """{ "unknownField" : "unknownValue" }"""

    @Autowired
    DefaultCurrencyExchangeIntegrationService currencyExchangeIntegrationService

    @Autowired
    private Converter<String, Optional<CurrencyExchangeRateDTO>> converter

    void "should return currency exchange for USD"() {
        when:
        var currencyOptional = currencyExchangeIntegrationService.getCurrencyExchangeRate(USD)

        then:
        assertCurrencyExchange(currencyOptional, USD)
        currencyOptional.get().getUsd() == "1"
    }

    void "should return currency exchange for EUR"() {
        when:
        var currencyOptional = currencyExchangeIntegrationService.getCurrencyExchangeRate(EUR)

        then:
        assertCurrencyExchange(currencyOptional, EUR)
        currencyOptional.get().getEur() == "1"
    }

    void "should return currency exchange for BYN"() {
        when:
        var currencyOptional = currencyExchangeIntegrationService.getCurrencyExchangeRate(BYN)

        then:
        assertCurrencyExchange(currencyOptional, BYN)
        currencyOptional.get().getByn() == "1"
    }

    void "should return currency exchange for RUB"() {
        when:
        var currencyOptional = currencyExchangeIntegrationService.getCurrencyExchangeRate(RUB)

        then:
        assertCurrencyExchange(currencyOptional, RUB)
        currencyOptional.get().getRub() == "1"
    }

    void "should return empty currency exchange given null currency"() {
        when:
        var currencyOptional = currencyExchangeIntegrationService.getCurrencyExchangeRate(null)

        then:
        !currencyOptional.isPresent()
    }

    void "should return empty currency exchange given unsuccessful API call"() {
        given:
        var webClient = buildMockWebClient(UNSUCCESSFUL_API_CALL_RESPONSE)
        var currencyExchangeService = new DefaultCurrencyExchangeIntegrationService(webClient, converter)

        when:
        var currencyOptional = currencyExchangeService.getCurrencyExchangeRate(BYN)

        then:
        !currencyOptional.isPresent()
    }

    void "should return empty currency exchange given unknown API response"() {
        given:
        var webClient = buildMockWebClient(UNKNOWN_API_CALL_RESPONSE)
        var currencyExchangeService = new DefaultCurrencyExchangeIntegrationService(webClient, converter)

        when:
        var currencyOptional = currencyExchangeService.getCurrencyExchangeRate(BYN)

        then:
        !currencyOptional.isPresent()
    }

    void "should return empty currency exchange given empty API response"() {
        given:
        var webClient = buildMockWebClient(EMPTY_STRING)
        var currencyExchangeService = new DefaultCurrencyExchangeIntegrationService(webClient, converter)

        when:
        var currencyOptional = currencyExchangeService.getCurrencyExchangeRate(BYN)

        then:
        !currencyOptional.isPresent()
    }

    boolean assertCurrencyExchange(Optional<CurrencyExchangeRateDTO> currencyOptional, Currency expectedCurrency) {
        assert currencyOptional.isPresent()

        var currency = currencyOptional.get()
        assert currency.getCode() == expectedCurrency.toString()
        assert !currency.getUsd().isEmpty()
        assert !currency.getEur().isEmpty()
        assert !currency.getByn().isEmpty()
        assert !currency.getRub().isEmpty()

        return true
    }

    WebClient buildMockWebClient(String body) {
        return WebClient.builder()
                .exchangeFunction(clientRequest ->
                        Mono.just(ClientResponse.create(HttpStatus.OK)
                                .header("content-type", "application/json")
                                .body(body)
                                .build()))
                .build()
    }
}
