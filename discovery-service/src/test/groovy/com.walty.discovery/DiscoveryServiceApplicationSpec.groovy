package com.walty.discovery

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import spock.lang.Specification

import static java.util.Objects.nonNull

@SpringBootTest
class DiscoveryServiceApplicationSpec extends Specification {

    @Autowired
    private ApplicationContext applicationContext

    void "should startup without exceptions"() {
        expect:
        nonNull(applicationContext)
    }
}
