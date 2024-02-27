package com.walty.currency.service.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest
class RoundingUtilUnitTest extends Specification {

    @Autowired
    private RoundingUtil roundingUtil

    @Unroll
    void "should round currency #value"() {
        given:
        var roundedValue = roundingUtil.round(value as double)

        expect:
        roundedValue == expectedValue

        where:
        value << [0, 1, 10, 1.1, 1.10, 1.101, 1.111_100, 1.111_111]
        expectedValue << [0, 1, 10, 1.1, 1.10, 1.101, 1.111_1, 1.111_1]
    }
}
