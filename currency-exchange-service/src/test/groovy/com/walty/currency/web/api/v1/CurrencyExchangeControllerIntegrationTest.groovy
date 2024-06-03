package com.walty.currency.web.api.v1

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.walty.currency.web.dto.CurrencyDTO
import com.walty.currency.web.dto.CurrencyValuesDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static com.walty.currency.config.Currency.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class CurrencyExchangeControllerIntegrationTest extends Specification {

    var CONVERT_CURRENCY_URL = "/v1/currency/exchange/"
    var GET_CURRENCY_VALUES_URL = "/v1/currency/values"

    var CODE_JSON_FIELD = "code"
    var VALUE_JSON_FIELD = "value"
    var USD_JSON_FIELD = USD.toString()
    var EUR_JSON_FIELD = EUR.toString()
    var BYN_JSON_FIELD = BYN.toString()
    var RUB_JSON_FIELD = RUB.toString()

    var VALUE = 100d

    @Autowired
    MockMvc mvc

    @Autowired
    ObjectMapper mapper

    void "should convert BYN to USD"() {
        when:
        var currencyToSell = new CurrencyDTO(BYN.toString(), VALUE)

        and:
        String json = mvc.perform(post(CONVERT_CURRENCY_URL + USD.toString())
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(currencyToSell)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString()

        then:
        var convertedCurrency = toCurrency(json)
        convertedCurrency.getCode() == USD.toString()
        convertedCurrency.getValue() > 0
    }

    void "should convert BYN to RUB"() {
        when:
        var currencyToSell = new CurrencyDTO(BYN.toString(), VALUE)

        and:
        String json = mvc.perform(post(CONVERT_CURRENCY_URL + RUB.toString())
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(currencyToSell)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString()

        then:
        var convertedCurrency = toCurrency(json)
        convertedCurrency.getCode() == RUB.toString()
        convertedCurrency.getValue() > 0
    }

    void "should convert BYN to EUR"() {
        when:
        var currencyToSell = new CurrencyDTO(BYN.toString(), VALUE)

        and:
        String json = mvc.perform(post(CONVERT_CURRENCY_URL + EUR.toString())
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(currencyToSell)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString()

        then:
        var convertedCurrency = toCurrency(json)
        convertedCurrency.getCode() == EUR.toString()
        convertedCurrency.getValue() > 0
    }

    void "should create currency values for USD"() {
        when:
        var currencyToSell = new CurrencyDTO(USD.toString(), VALUE)

        and:
        String json = mvc.perform(post(GET_CURRENCY_VALUES_URL)
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(currencyToSell)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString()

        then:
        var convertedCurrency = toCurrencyValues(json)
        convertedCurrency.getUsd() == VALUE
        convertedCurrency.getEur() > 0
        convertedCurrency.getByn() > 0
        convertedCurrency.getRub() > 0
    }

    void "should create currency values for EUR"() {
        when:
        var currencyToSell = new CurrencyDTO(EUR.toString(), VALUE)

        and:
        String json = mvc.perform(post(GET_CURRENCY_VALUES_URL)
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(currencyToSell)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString()

        then:
        var convertedCurrency = toCurrencyValues(json)
        convertedCurrency.getUsd() > 0
        convertedCurrency.getEur() == VALUE
        convertedCurrency.getByn() > 0
        convertedCurrency.getRub() > 0
    }

    void "should create currency values for BYN"() {
        when:
        var currencyToSell = new CurrencyDTO(BYN.toString(), VALUE)

        and:
        String json = mvc.perform(post(GET_CURRENCY_VALUES_URL)
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(currencyToSell)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString()

        then:
        var convertedCurrency = toCurrencyValues(json)
        convertedCurrency.getUsd() > 0
        convertedCurrency.getEur() > 0
        convertedCurrency.getByn() == VALUE
        convertedCurrency.getRub() > 0
    }

    void "should create currency values for RUB"() {
        when:
        var currencyToSell = new CurrencyDTO(RUB.toString(), VALUE)

        and:
        String json = mvc.perform(post(GET_CURRENCY_VALUES_URL)
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(currencyToSell)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString()

        then:
        var convertedCurrency = toCurrencyValues(json)
        convertedCurrency.getUsd() > 0
        convertedCurrency.getEur() > 0
        convertedCurrency.getByn() > 0
        convertedCurrency.getRub() == VALUE
    }

    String asJsonString(Object obj) {
        return mapper.writeValueAsString(obj)
    }

    CurrencyDTO toCurrency(String json) {
        JsonNode jsonNode = mapper.readTree(json)

        var convertedCurrency = new CurrencyDTO()
        convertedCurrency.setCode(jsonNode.get(CODE_JSON_FIELD).asText())
        convertedCurrency.setValue(jsonNode.get(VALUE_JSON_FIELD).asDouble())

        return convertedCurrency
    }

    CurrencyValuesDTO toCurrencyValues(String json) {
        JsonNode jsonNode = mapper.readTree(json)

        var currencyValues = new CurrencyValuesDTO()
        currencyValues.setUsd(jsonNode.get(USD_JSON_FIELD).asDouble())
        currencyValues.setEur(jsonNode.get(EUR_JSON_FIELD).asDouble())
        currencyValues.setByn(jsonNode.get(BYN_JSON_FIELD).asDouble())
        currencyValues.setRub(jsonNode.get(RUB_JSON_FIELD).asDouble())

        return currencyValues
    }
}
