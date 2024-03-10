package com.walty.telegram.web.telegram.command.impl;

import com.walty.telegram.web.telegram.command.Command;
import com.walty.telegram.web.dto.TransactionRecordDTO;
import com.walty.telegram.web.integration.TransactionRecordWebClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Component
@AllArgsConstructor
public class GetTransactionRecordsCommand implements Command {

    private static final String NO_TRANSACTIONS_FOUND_RESPONSE = "No transaction records found";

    private static final String TRANSACTION_RECORD_RESPONSE = """
                                                              id: %s
                                                              date: %s
                                                              description: %s
                                                              byn: %s
                                                              """;

    private static final String SEPARATOR = "----------------\n";

    private TransactionRecordWebClient transactionRecordWebClient;

    @Override
    public String execute(long telegramChatId, String input) {
        var transactionRecords = transactionRecordWebClient.getTransactionRecordsUri(String.valueOf(telegramChatId));

        if (transactionRecords.isEmpty()) {
            return NO_TRANSACTIONS_FOUND_RESPONSE;
        }

        return createTransactionRecordsResponse(transactionRecords);
    }

    private String createTransactionRecordsResponse(List<TransactionRecordDTO> transactionRecords) {
        var builder = new StringBuilder(SEPARATOR);
        transactionRecords.stream()
                .sorted(Comparator.comparing(TransactionRecordDTO::getDate).reversed())
                .forEach(transaction -> {
                    var date = transaction.getDate().toInstant()
                            .atOffset(ZoneOffset.UTC)
                            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME )
                            .replace("T" , " ");

                    builder.append(String.format(TRANSACTION_RECORD_RESPONSE, transaction.getId(), date, transaction.getDescription(), transaction.getByn()));
                    builder.append(SEPARATOR);
                });

        return builder.toString();
    }
}
