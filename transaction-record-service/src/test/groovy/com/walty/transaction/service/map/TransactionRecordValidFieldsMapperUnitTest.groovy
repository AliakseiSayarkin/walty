package com.walty.transaction.service.map

import com.walty.transaction.service.mapper.TransactionRecordValidFieldsMapper
import com.walty.transaction.service.model.TransactionRecordModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class TransactionRecordValidFieldsMapperUnitTest extends Specification {

    var SOURCE_CURRENCY_VALUE = 100d
    var TARGET_CURRENCY_VALUE = 200d

    var SOURCE_DATE = new Date()
    var TARGET_DATE = new Date()

    var SOURCE_DESCRIPTION = "source description"
    var TARGET_DESCRIPTION = "target description"

    TransactionRecordModel sourceTransactionRecord
    TransactionRecordModel targetTransactionRecord

    @Autowired
    TransactionRecordValidFieldsMapper transactionRecordValidFieldsMapper

    void setup() {
        sourceTransactionRecord = TransactionRecordModel.builder()
                .date(SOURCE_DATE)
                .description(SOURCE_DESCRIPTION)
                .usd(SOURCE_CURRENCY_VALUE)
                .eur(SOURCE_CURRENCY_VALUE)
                .byn(SOURCE_CURRENCY_VALUE)
                .rub(SOURCE_CURRENCY_VALUE)
                .build()

        targetTransactionRecord = TransactionRecordModel.builder()
                .date(TARGET_DATE)
                .description(TARGET_DESCRIPTION)
                .usd(TARGET_CURRENCY_VALUE)
                .eur(TARGET_CURRENCY_VALUE)
                .byn(TARGET_CURRENCY_VALUE)
                .rub(TARGET_CURRENCY_VALUE)
                .build()

    }

    void "should map fields"() {
        when:
        transactionRecordValidFieldsMapper.mapValidFields(sourceTransactionRecord, targetTransactionRecord)

        then:
        sourceTransactionRecord.getDate() == targetTransactionRecord.getDate()
        sourceTransactionRecord.getDescription() == targetTransactionRecord.getDescription()
        sourceTransactionRecord.getUsd() == targetTransactionRecord.getUsd()
        sourceTransactionRecord.getEur() == targetTransactionRecord.getEur()
        sourceTransactionRecord.getByn() == targetTransactionRecord.getByn()
        sourceTransactionRecord.getRub() == targetTransactionRecord.getRub()
    }

    void "should map only valid fields given transaction record with empty fields"() {
        given:
        sourceTransactionRecord.setDescription(null)
        sourceTransactionRecord.setByn(0)

        when:
        transactionRecordValidFieldsMapper.mapValidFields(sourceTransactionRecord, targetTransactionRecord)

        then:
        TARGET_CURRENCY_VALUE == targetTransactionRecord.getByn()
        TARGET_DESCRIPTION == targetTransactionRecord.getDescription()

        sourceTransactionRecord.getDate() == targetTransactionRecord.getDate()
        sourceTransactionRecord.getUsd() == targetTransactionRecord.getUsd()
        sourceTransactionRecord.getEur() == targetTransactionRecord.getEur()
        sourceTransactionRecord.getRub() == targetTransactionRecord.getRub()
    }

    void "should not map fields given empty transaction record"() {
        given:
        sourceTransactionRecord = new TransactionRecordModel()

        when:
        transactionRecordValidFieldsMapper.mapValidFields(sourceTransactionRecord, targetTransactionRecord)

        then:
        TARGET_DATE == targetTransactionRecord.getDate()
        TARGET_DESCRIPTION == targetTransactionRecord.getDescription()
        TARGET_CURRENCY_VALUE == targetTransactionRecord.getUsd()
        TARGET_CURRENCY_VALUE == targetTransactionRecord.getEur()
        TARGET_CURRENCY_VALUE == targetTransactionRecord.getByn()
        TARGET_CURRENCY_VALUE == targetTransactionRecord.getRub()
    }
}
