package com.walty.discovery

import spock.lang.Specification

class DiscoveryServiceApplicationSpec extends Specification {

    void "should startup without exceptions"() {
        expect:
        new DiscoveryServiceApplication()
    }
}
