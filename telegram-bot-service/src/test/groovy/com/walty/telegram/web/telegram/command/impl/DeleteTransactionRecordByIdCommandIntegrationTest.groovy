package com.walty.telegram.web.telegram.command.impl

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.walty.telegram.service.ParsingService
import com.walty.telegram.web.integration.TransactionRecordWebClient
import com.walty.telegram.web.telegram.command.validator.CommandValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class DeleteTransactionRecordByIdCommandIntegrationTest extends Specification {

    var SUCCESS_RESPONSE = "Transaction record with id: %s successfully deleted"

    var ID = 1
    var TELEGRAM_CHAT_ID = 1l

    var VALID_COMMAND_WITH_TELEGRAM_CHAT_ID = "/deleteTransactionRecordById 1"

    @Autowired
    @Collaborator
    ParsingService parsingService

    @Autowired
    @Collaborator
    CommandValidator deleteTransactionRecordByIdCommandValidator

    @Collaborator
    TransactionRecordWebClient transactionRecordWebClient = Mock()

    @Subject
    DeleteTransactionRecordByIdCommand deleteTransactionRecordByIdCommand

    void setup() {
        when:
        transactionRecordWebClient.deleteTransactionRecordById(ID) >> { }
    }

    void "should delete transaction record by id given valid command"() {
        when:
        var response = deleteTransactionRecordByIdCommand.execute(TELEGRAM_CHAT_ID, VALID_COMMAND_WITH_TELEGRAM_CHAT_ID)

        then:
        String.format(SUCCESS_RESPONSE, ID) == response
    }

    void "should throw NullPointerException when execute() given null command"() {
        when:
        deleteTransactionRecordByIdCommand.execute(TELEGRAM_CHAT_ID, null)

        then:
        thrown NullPointerException
    }
}
