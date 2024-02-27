package com.walty.currency.service.integration.converter

import com.walty.currency.Currency
import com.walty.currency.service.integration.dto.CurrencyExchangeRateDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static com.walty.currency.Currency.*

@SpringBootTest
class CurrencyRatesJsonConverterUnitTest extends Specification {

    @Shared
    var USD_VALID_SUCCESSFUL_JSON = """
                                        {
                                            "result": "success",
                                            "base_code": "USD",
                                            "rates": {
                                                "BYN": 1,
                                                "USD": 2,
                                                "EUR": 3,
                                                "RUB": 4
                                            }
                                        } 
                                   """

    @Shared
    var EUR_VALID_SUCCESSFUL_JSON = """
                                        {
                                            "result": "success",
                                            "base_code": "EUR",
                                            "rates": {
                                                "BYN": 1,
                                                "USD": 2,
                                                "EUR": 3,
                                                "RUB": 4
                                            }
                                        } 
                                   """

    @Shared
    var BYN_VALID_SUCCESSFUL_JSON = """
                                        {
                                            "result": "success",
                                            "base_code": "BYN",
                                            "rates": {
                                                "BYN": 1,
                                                "USD": 2,
                                                "EUR": 3,
                                                "RUB": 4
                                            }
                                        } 
                                   """

    @Shared
    var RUB_VALID_SUCCESSFUL_JSON = """
                                        {
                                            "result": "success",
                                            "base_code": "RUB",
                                            "rates": {
                                                "BYN": 1,
                                                "USD": 2,
                                                "EUR": 3,
                                                "RUB": 4
                                            }
                                        } 
                                   """

    var INVALID_JSON = """
                                       ／＞　 フ
                                      | 　_　_| 
                                    ／` ミ__^ノ 
                                   /　　　　 |
                                 /　 ヽ　　 ﾉ           ╱|、
                                /　　 |　|　|         (˚ˎ 。7  
                            ／￣|　　 |　|　|          |、˜〵          
                            (￣ヽ＿_  ヽ_)__)         じしˍ,)ノ
                            ＼二)
                       """

    var VALID_UNSUCCESSFUL_JSON = """
                                        {
                                            "result": "failure",
                                            "base_code": "USD",
                                            "rates": {
                                                "BYN": 1,
                                                "USD": 2,
                                                "EUR": 3,
                                                "RUB": 4
                                            }
                                        } 
                                   """

    var INVALID_SUCCESSFUL_JSON = """
                                        {
                                            "result": "success"
                                        } 
                                   """

    @Autowired
    CurrencyRatesJsonConverter currencyRatesJsonConverter

    @Unroll
    void "should return currency exchange given valid successful #json of a #currency"() {
        given:
        var currencyOptional = currencyRatesJsonConverter.convert(json)

        expect:
        assertValidCurrencyExchange(currencyOptional, currency)

        where:
        currency << [USD, EUR, BYN, RUB]
        json << [USD_VALID_SUCCESSFUL_JSON, EUR_VALID_SUCCESSFUL_JSON, BYN_VALID_SUCCESSFUL_JSON, RUB_VALID_SUCCESSFUL_JSON]
    }

    void "should return empty currency exchange given incorrect json"() {
        when:
        var currencyOptional = currencyRatesJsonConverter.convert(INVALID_JSON)

        then:
        !currencyOptional.isPresent()
    }

    void "should return empty currency exchange given valid unsuccessful json"() {
        when:
        var currencyOptional = currencyRatesJsonConverter.convert(VALID_UNSUCCESSFUL_JSON)

        then:
        !currencyOptional.isPresent()
    }

    void "should return empty currency exchange given invalid successful json"() {
        when:
        var currencyOptional = currencyRatesJsonConverter.convert(INVALID_SUCCESSFUL_JSON)

        then:
        !currencyOptional.isPresent()
    }


    boolean assertValidCurrencyExchange(Optional<CurrencyExchangeRateDTO> currencyOptional, Currency currencyEnum) {
        assert currencyOptional.isPresent()

        var currency = currencyOptional.get()
        assert currency.getCode() == currencyEnum.toString()
        assert currency.getByn() == "1"
        assert currency.getUsd() == "2"
        assert currency.getEur() == "3"
        assert currency.getRub() == "4"

        return true
    }
}
