package com.walty.currency

import spock.lang.Specification

class CurrencyExchangeServiceApplicationSpec extends Specification {

    void "should startup without exceptions"() {
        expect:
        new CurrencyExchangeServiceApplication()
    }
}