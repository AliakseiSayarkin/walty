package com.walty.telegram.web.telegram.command.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class HelpCommandUnitTest extends Specification {

    var HELP_RESPONSE = """
                        |Available commands:
                        |/getTransactionRecords - get all transaction records
                        |/saveTransactionRecord - save transaction record
                        |/deleteAllTransactionRecords - delete all transaction records
                        |/deleteTransactionRecordById - delete transaction record record by id
                        |
                        |/exchange - get real-time currency exchange rates
                        |
                        |/help - get all available commands with their description
                        """.stripMargin().trim()

    @Autowired
    HelpCommand helpCommand

    void "should return help response"() {
        when:
        var response = helpCommand.execute(0l, null).trim()

        then:
        HELP_RESPONSE == response
    }
}
