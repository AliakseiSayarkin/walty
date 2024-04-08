package com.walty.telegram.web.telegram.command.impl

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.walty.telegram.web.integration.TransactionRecordWebClient
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class DeleteAllTransactionRecordsCommandUnitTest extends Specification {

    var SUCCESS_RESPONSE = "All transaction records were successfully deleted"

    var TELEGRAM_CHAT_ID = 1l
    var INPUT = ""

    @Collaborator
    TransactionRecordWebClient transactionRecordWebClient = Mock()

    @Subject
    DeleteAllTransactionRecordsCommand deleteAllTransactionRecordsCommand

    void setup() {
        when:
        transactionRecordWebClient.deleteAllTransactionRecord(TELEGRAM_CHAT_ID) >> { }
    }

    void "should delete all transaction records"() {
        when:
        var response = deleteAllTransactionRecordsCommand.execute(TELEGRAM_CHAT_ID, INPUT)

        then:
        SUCCESS_RESPONSE == response
    }
}
