package com.walty.currency.facade.converter

import com.walty.currency.service.model.CurrencyModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static com.walty.currency.Currency.USD

@SpringBootTest
class CurrencyDTOConverterUnitTest extends Specification {

    var VALUE = 1d

    var VALID_CURRENCY = new CurrencyModel(USD, VALUE)

    @Autowired
    CurrencyDTOConverter currencyDTOConverter

    void "should convert currency"() {
        when:
        var currency = currencyDTOConverter.convert(VALID_CURRENCY)

        then:
        currency.getValue() == VALUE
        currency.getCode() == USD.toString()
    }

    void "should throw NullPointerException when convert() given null currency"() {
        when:
        currencyDTOConverter.convert(null)
        then:
        thrown NullPointerException
    }
}
