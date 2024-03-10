package com.walty.telegram.web.telegram.command.impl

import com.walty.telegram.web.telegram.command.impl.DefaultResponseCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class DefaultResponseCommandUnitTest extends Specification {

    var DEFAULT_RESPONSE_TO_UNKNOWN_COMMAND = "Unknown command, please use /help for available commands"

    @Autowired
    DefaultResponseCommand defaultResponseCommand

    void "should return default response response"() {
        when:
        var response = defaultResponseCommand.execute(0l, null)

        then:
        DEFAULT_RESPONSE_TO_UNKNOWN_COMMAND == response
    }
}
