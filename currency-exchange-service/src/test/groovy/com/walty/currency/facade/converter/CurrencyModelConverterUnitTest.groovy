package com.walty.currency.facade.converter

import com.walty.currency.web.dto.CurrencyDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static com.walty.currency.Currency.USD

@SpringBootTest
class CurrencyModelConverterUnitTest extends Specification {

    var VALUE = 1d

    var VALID_CURRENCY = new CurrencyDTO(USD.toString(), VALUE)

    @Autowired
    CurrencyModelConverter currencyModelConverter

    void "should convert valid currency"() {
        when:
        var currency = currencyModelConverter.convert(VALID_CURRENCY)

        then:
        currency.getValue() == VALUE
        currency.getCode() == USD
    }

    void "should throw NullPointerException given null currency"() {
        when:
        currencyModelConverter.convert(null)
        then:
        thrown NullPointerException
    }
}