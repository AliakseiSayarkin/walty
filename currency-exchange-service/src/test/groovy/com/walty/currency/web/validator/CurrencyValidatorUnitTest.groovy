package com.walty.currency.web.validator

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class CurrencyValidatorUnitTest extends Specification {

    var VALID_CURRENCY = "BYN"
    var INVALID_CURRENCY = """
                                    ╱|、  ?
                                  (˚ˎ 。7  
                                   |、˜〵          
                                   じしˍ,)ノ
                            """

    var currencyValidator = new CurrencyValidator()

    void setup() {
        currencyValidator.initialize((ValidCurrency) null)
    }

    void "should return that BYN is valid currency"() {
        when:
        var result = currencyValidator.isValid(VALID_CURRENCY, null)

        then:
        result
    }

    void "should return that 'ฅ^._.^ฅ' is invalid currency"() {
        when:
        var result = currencyValidator.isValid(INVALID_CURRENCY, null)

        then:
        !result
    }
}
