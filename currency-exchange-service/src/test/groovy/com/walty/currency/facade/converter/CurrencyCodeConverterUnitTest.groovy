package com.walty.currency.facade.converter

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static com.walty.currency.config.Currency.*

@SpringBootTest
class CurrencyCodeConverterUnitTest extends Specification {

    @Shared
    var USD_CODE = "USD"
    @Shared
    var EUR_CODE = "EUR"
    @Shared
    var BYN_CODE = "BYN"
    @Shared
    var RUB_CODE = "RUB"

    var UNKNOWN_CODE = """
                               /\\_/\\    ♡
                             >(• ༝ • )< 
                               |      \\
                               |  |  |   )_
                               l--l- --/ / 
                                       |  |
                                        \\ \\
                                          \\/
                       """

    @Autowired
    CurrencyCodeConverter currencyCodeConverter

    @Unroll
    void "should convert #currencyCode to #currencyEnum"() {
        expect:
        currencyEnum == currencyCodeConverter.convert(currencyCode)

        where:
        currencyCode << [USD_CODE, EUR_CODE, BYN_CODE, RUB_CODE]
        currencyEnum << [USD, EUR, BYN, RUB]
    }

    void "should throw NullPointerException when convert() given null currency code"() {
        when:
        currencyCodeConverter.convert(null)

        then:
        thrown NullPointerException
    }

    void "should throw IllegalArgumentException when convert() given unknown currency code"() {
        when:
        currencyCodeConverter.convert(UNKNOWN_CODE)

        then:
        thrown IllegalArgumentException
    }
}
