package com.walty.transaction.facade.converter

import com.walty.transaction.service.model.TransactionRecordModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static java.util.Objects.nonNull

@SpringBootTest
class TransactionRecordDTOConverterUnitTest extends Specification {

    @Autowired
    TransactionRecordDTOConverter transactionRecordDTOConverter

    var ID = "1"
    var TELEGRAM_CHAT_ID = "1"
    var DATE = new Date()
    var DESCRIPTION = "description"
    var USD_VALUE = 100d
    var EUR_VALUE = 200d
    var BYN_VALUE = 300d
    var RUB_VALUE = 400d

    void "should convert transaction record model to DTO"() {
        given:
        var transactionRecordModel = TransactionRecordModel.builder()
                .id(ID)
                .telegramChatId(TELEGRAM_CHAT_ID)
                .date(DATE)
                .description(DESCRIPTION)
                .usd(USD_VALUE)
                .eur(EUR_VALUE)
                .byn(BYN_VALUE)
                .rub(RUB_VALUE)
                .build()

        when:
        var transactionRecordDTO = transactionRecordDTOConverter.convert(transactionRecordModel)

        then:
        nonNull(transactionRecordDTO)
        transactionRecordModel.getId() == transactionRecordDTO.getId()
        transactionRecordModel.getId() == transactionRecordDTO.getId()
        transactionRecordModel.getTelegramChatId() == transactionRecordDTO.getTelegramChatId()
        transactionRecordModel.getDate() == transactionRecordDTO.getDate()
        transactionRecordModel.getDescription() == transactionRecordDTO.getDescription()
        transactionRecordModel.getUsd() == transactionRecordDTO.getUsd()
        transactionRecordModel.getEur() == transactionRecordDTO.getEur()
        transactionRecordModel.getByn() == transactionRecordDTO.getByn()
        transactionRecordModel.getRub() == transactionRecordDTO.getRub()
    }

    void "should throw NullPointerException when convert() given null transaction record model"() {
        when:
        transactionRecordDTOConverter.convert(null)

        then:
        thrown NullPointerException
    }
}
