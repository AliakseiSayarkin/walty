package com.walty.transaction.web.api.v1

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.walty.transaction.service.model.TransactionRecordModel
import com.walty.transaction.web.dto.TransactionRecordDTO
import com.walty.transaction.web.dto.TransactionSummaryDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Shared
import spock.lang.Specification

import java.text.SimpleDateFormat

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class TransactionRecordControllerIntegrationTest extends Specification {

    var TRANSACTION_RECORD_BASE_URL = "/v1/transaction"
    var GET_CURRENT_MONTH_TRANSACTION_RECORDS_URL = TRANSACTION_RECORD_BASE_URL + "/date/%s/"
    var GET_TRANSACTION_SUMMARY_URL = TRANSACTION_RECORD_BASE_URL + "/date/%s/summary/"
    var DELETE_TRANSACTION_RECORD_BY_ID_URL = TRANSACTION_RECORD_BASE_URL + "/id/"
    var DELETE_TRANSACTION_RECORD_BY_TELEGRAM_CHAT_ID_URL = TRANSACTION_RECORD_BASE_URL + "/telegram/chat/id/"

    @Shared
    var IDS = ["1", "2", "3", "4", "5"]

    var DATE = new Date()
    var DESCRIPTION = "description"
    var USD_VALUE = 100d
    var EUR_VALUE = 200d
    var BYN_VALUE = 300d
    var RUB_VALUE = 400d

    var TELEGRAM_CHAT_ID_TO_SAVE = "6"

    @Autowired
    MockMvc mvc

    @Autowired
    ObjectMapper mapper

    @Autowired
    MongoTemplate mongoTemplate

    void setup() {
        mongoTemplate.dropCollection(TransactionRecordModel.class)

        IDS.forEach { id ->
            {
                var transactionRecord = TransactionRecordModel.builder()
                        .id(id)
                        .telegramChatId(id)
                        .date(DATE)
                        .description(DESCRIPTION)
                        .usd(USD_VALUE)
                        .eur(EUR_VALUE)
                        .byn(BYN_VALUE)
                        .rub(RUB_VALUE)
                        .build()

                mongoTemplate.save(transactionRecord)
            }
        }
    }

    void "should return transactions records by telegram chat id"() {
        when:
        String json = mvc.perform(get(String.format(GET_CURRENT_MONTH_TRANSACTION_RECORDS_URL, "current") + IDS.getFirst()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString()

        then:
        var returnedTransactionRecords = deserializeTransactionRecords(json)

        1 == returnedTransactionRecords.size()
        IDS.getFirst() == returnedTransactionRecords.getFirst().getTelegramChatId()
    }

    void "should return 404 Not Found when returning transaction records given empty telegram chat id"() {
        when:
        mvc.perform(get(GET_CURRENT_MONTH_TRANSACTION_RECORDS_URL + " "))
                .andExpect(status().isNotFound())

        then:
        noExceptionThrown()
    }

    void "should return transactions records by telegram chat id and date"() {
        given:
        var date = formatDateToString(DATE)

        when:
        String json = mvc.perform(get(String.format(GET_CURRENT_MONTH_TRANSACTION_RECORDS_URL, date) + IDS.getFirst()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString()

        then:
        var returnedTransactionRecords = deserializeTransactionRecords(json)

        1 == returnedTransactionRecords.size()
        IDS.getFirst() == returnedTransactionRecords.getFirst().getTelegramChatId()
    }

    void "should return transaction summary for telegram chat id and date"() {
        given:
        var date = formatDateToString(DATE)

        when:
        String json = mvc.perform(get(String.format(GET_TRANSACTION_SUMMARY_URL, date) + IDS.getFirst()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString()

        then:
        var returnedTransactionRecords = deserializeTransactionSummary(json)

        IDS.getFirst() == returnedTransactionRecords.getTelegramChatId()
        USD_VALUE == returnedTransactionRecords.getUsd()
        EUR_VALUE == returnedTransactionRecords.getEur()
        BYN_VALUE == returnedTransactionRecords.getByn()
        RUB_VALUE == returnedTransactionRecords.getRub()
    }

    void "should return 404 Not Found when returning transaction summary given empty telegram chat id and date"() {
        given:
        var date = null

        when:
        mvc.perform(get(String.format(GET_TRANSACTION_SUMMARY_URL, date) + " "))
                .andExpect(status().isNotFound())

        then:
        noExceptionThrown()
    }

    void "should save transaction record"() {
        given:
        var transactionRecordToSave = TransactionRecordDTO.builder()
                .telegramChatId(TELEGRAM_CHAT_ID_TO_SAVE)
                .date(DATE)
                .description(DESCRIPTION)
                .usd(USD_VALUE)
                .eur(EUR_VALUE)
                .byn(BYN_VALUE)
                .rub(RUB_VALUE)
                .build()

        when:
        String json = mvc.perform(post(TRANSACTION_RECORD_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON).content(formatTransactionRecordToString(transactionRecordToSave)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString()

        then:
        var savedTransactionRecord = deserializeTransactionRecord(json)

        TELEGRAM_CHAT_ID_TO_SAVE == savedTransactionRecord.getTelegramChatId()
    }

    void "should return 404 Not Found when saving transaction record given null transaction record"() {
        given:
        var transactionRecordToSave = null

        when:
        mvc.perform(post(TRANSACTION_RECORD_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON).content(formatTransactionRecordToString(transactionRecordToSave)))
                .andExpect(status().isNotFound())

        then:
        noExceptionThrown()
    }

    void "should return 404 Not Found when saving transaction record given invalid transaction record"() {
        given:
        var transactionRecordToSave = TransactionRecordDTO.builder()
                .id(IDS.getFirst())
                .telegramChatId(TELEGRAM_CHAT_ID_TO_SAVE)
                .date(DATE)
                .description(DESCRIPTION)
                .usd(USD_VALUE)
                .eur(EUR_VALUE)
                .byn(BYN_VALUE)
                .rub(RUB_VALUE)
                .build()

        when:
        mvc.perform(post(TRANSACTION_RECORD_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON).content(formatTransactionRecordToString(transactionRecordToSave)))
                .andExpect(status().isNotFound())

        then:
        noExceptionThrown()
    }

    void "should return transactions records by telegram chat id"() {
        when:
        mvc.perform(delete(DELETE_TRANSACTION_RECORD_BY_ID_URL + IDS.getFirst()))
                .andExpect(status().isOk())

        then:
        noExceptionThrown()
    }

    void "should delete transaction record by id"() {
        when:
        mvc.perform(delete(DELETE_TRANSACTION_RECORD_BY_ID_URL + IDS.getFirst()))
                .andExpect(status().isOk())

        then:
        noExceptionThrown()
    }

    void "should return 404 Not Found when deleting transaction record given empty id"() {
        when:
        mvc.perform(delete(DELETE_TRANSACTION_RECORD_BY_ID_URL + " "))
                .andExpect(status().isNotFound())

        then:
        noExceptionThrown()
    }

    void "should delete transaction record by telegram chat id"() {
        when:
        mvc.perform(delete(DELETE_TRANSACTION_RECORD_BY_TELEGRAM_CHAT_ID_URL + IDS.getFirst()))
                .andExpect(status().isOk())

        then:
        noExceptionThrown()
    }

    void "should return 404 Not Found when deleting transaction record given empty telegram chat id"() {
        when:
        mvc.perform(delete(DELETE_TRANSACTION_RECORD_BY_TELEGRAM_CHAT_ID_URL + " "))
                .andExpect(status().isNotFound())

        then:
        noExceptionThrown()
    }

    void "should update transaction record"() {
        given:
        var transactionRecordToSave = TransactionRecordDTO.builder()
                .id(IDS.getFirst())
                .telegramChatId(TELEGRAM_CHAT_ID_TO_SAVE)
                .date(DATE)
                .description(DESCRIPTION)
                .usd(USD_VALUE)
                .eur(EUR_VALUE)
                .byn(BYN_VALUE)
                .rub(RUB_VALUE)
                .build()

        when:
        String json = mvc.perform(put(TRANSACTION_RECORD_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON).content(formatTransactionRecordToString(transactionRecordToSave)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString()

        then:
        var savedTransactionRecord = deserializeTransactionRecord(json)

        savedTransactionRecord.getId() == IDS.getFirst()
        savedTransactionRecord.getTelegramChatId() == TELEGRAM_CHAT_ID_TO_SAVE
    }

    void "should return 404 Not Found when updating transaction record given null transaction record"() {
        given:
        var transactionRecordToSave = null

        when:
        mvc.perform(put(TRANSACTION_RECORD_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON).content(formatTransactionRecordToString(transactionRecordToSave)))
                .andExpect(status().isNotFound())

        then:
        noExceptionThrown()
    }

    void "should return 404 Not Found when updating transaction record given invalid transaction record"() {
        given:
        var transactionRecordToSave = TransactionRecordDTO.builder()
                .telegramChatId(TELEGRAM_CHAT_ID_TO_SAVE)
                .date(DATE)
                .description(DESCRIPTION)
                .usd(USD_VALUE)
                .eur(EUR_VALUE)
                .byn(BYN_VALUE)
                .rub(RUB_VALUE)
                .build()

        when:
        mvc.perform(put(TRANSACTION_RECORD_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON).content(formatTransactionRecordToString(transactionRecordToSave)))
                .andExpect(status().isNotFound())

        then:
        noExceptionThrown()
    }

    void "should return 404 Not Found when updating transaction record given not existing transaction record"() {
        given:
        var transactionRecordToSave = TransactionRecordDTO.builder()
                .id(TELEGRAM_CHAT_ID_TO_SAVE)
                .telegramChatId(TELEGRAM_CHAT_ID_TO_SAVE)
                .date(DATE)
                .description(DESCRIPTION)
                .usd(USD_VALUE)
                .eur(EUR_VALUE)
                .byn(BYN_VALUE)
                .rub(RUB_VALUE)
                .build()

        when:
        mvc.perform(put(TRANSACTION_RECORD_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON).content(formatTransactionRecordToString(transactionRecordToSave)))
                .andExpect(status().isNotFound())

        then:
        noExceptionThrown()
    }

    String formatDateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date)
    }

    List<TransactionRecordDTO> deserializeTransactionRecords(String json) {
        return new ObjectMapper().readValue(json, new TypeReference<List<TransactionRecordDTO>>() {})
    }

    TransactionSummaryDTO deserializeTransactionSummary(String json) {
        return new ObjectMapper().readValue(json, new TypeReference<TransactionSummaryDTO>() {})
    }

    TransactionRecordDTO deserializeTransactionRecord(String json) {
        return new ObjectMapper().readValue(json, new TypeReference<TransactionRecordDTO>() {})
    }

    String formatTransactionRecordToString(TransactionRecordDTO transactionRecord) {
        return new ObjectMapper().writeValueAsString(transactionRecord)
    }
}
