package com.walty.telegram.web.telegram.command.impl

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.walty.telegram.web.dto.TransactionRecordDTO
import com.walty.telegram.web.integration.TransactionRecordWebClient
import com.walty.telegram.web.telegram.command.impl.GetTransactionRecordsCommand
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class GetTransactionRecordsCommandUnitTest extends Specification {

    var NO_TRANSACTIONS_FOUND_RESPONSE = "No transaction records found"

    var TRANSACTION_RECORD_RESPONSE = """
                                      |----------------
                                      |id: 1
                                      |date: 1970-01-01 00:00:00
                                      |description: description
                                      |byn: 100.0
                                      |----------------
                                      |id: 2
                                      |date: 1970-01-01 00:00:00
                                      |description: description
                                      |byn: 100.0
                                      |----------------
                                      """.stripMargin().trim()

    var IDS = ["1", "2"]
    var CURRENCY_VALUE = 100d
    var DESCRIPTION = "description"

    @Collaborator
    TransactionRecordWebClient transactionRecordWebClient = Mock()

    @Subject
    GetTransactionRecordsCommand getTransactionRecordsCommand

    void setup() {
        given:
        var date = new Date(0)

        and:
        var transactionRecords = IDS.stream()
                .map {id ->
                    TransactionRecordDTO.builder()
                        .id(id)
                        .telegramChatId(id)
                        .date(date)
                        .usd(CURRENCY_VALUE)
                        .eur(CURRENCY_VALUE)
                        .byn(CURRENCY_VALUE)
                        .rub(CURRENCY_VALUE)
                        .description(DESCRIPTION)
                        .build()
                }
                .toList()

        when:
        transactionRecordWebClient.getTransactionRecordsUri(_ as String) >> transactionRecords
    }

    void "should return transaction records" () {
        when:
        var result = getTransactionRecordsCommand.execute(0l, "").trim()

        then:
        TRANSACTION_RECORD_RESPONSE == result
    }

    void "should return empty transaction records when transactions returns empty list" () {
        when:
        var result = getTransactionRecordsCommand.execute(0l, "").trim()

        then:
        NO_TRANSACTIONS_FOUND_RESPONSE == result

        and:
        transactionRecordWebClient.getTransactionRecordsUri(_ as String) >> Collections.emptyList()
    }
}
