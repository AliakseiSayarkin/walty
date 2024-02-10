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
    CurrencyDataConverter currencyDataConverter

    void "should convert valid currency"() {
        when:
        var currency = currencyDataConverter.convert(VALID_CURRENCY)

        then:
        currency.getValue() == VALUE
        currency.getCode() == USD.toString()
    }

    void "should throw NullPointerException given null currency"() {
        when:
        currencyDataConverter.convert(null)
        then:
        thrown NullPointerException
    }
}
