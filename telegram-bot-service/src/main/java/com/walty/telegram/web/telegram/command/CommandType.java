package com.walty.telegram.web.telegram.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandType {

    GET_TRANSACTION_RECORDS("getTransactionRecords"),
    SAVE_TRANSACTION_RECORDS("saveTransactionRecord"),
    DELETE_ALL_TRANSACTION_RECORDS("deleteAllTransactionRecords"),
    DELETE_TRANSACTION_RECORD_BY_ID("deleteTransactionRecordById"),
    EXCHANGE("exchange"),
    HELP("help"),
    DEFAULT("default");

    final String commandName;
}
