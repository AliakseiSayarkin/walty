package com.walty.transaction.web.api.v1;

import com.walty.transaction.facade.TransactionRecordFacade;
import com.walty.transaction.web.dto.TransactionRecordDTO;
import com.walty.transaction.web.dto.TransactionSummaryDTO;
import com.walty.transaction.web.validator.DateValidator;
import com.walty.transaction.web.validator.TransactionRecordValidator;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/transaction")
@AllArgsConstructor
public class TransactionRecordController {

    private final TransactionRecordFacade transactionRecordFacade;

    private final DateValidator dateValidator;
    private final TransactionRecordValidator transactionRecordValidator;

    @GetMapping("/date/current/{telegramChatId}")
    ResponseEntity<List<TransactionRecordDTO>> getCurrentMonthTransactionRecordsByTelegramChatId(@NotBlank @PathVariable String telegramChatId) {
        return ResponseEntity.ofNullable(transactionRecordFacade.getCurrentMonthTransactionRecordsByTelegramChatId(telegramChatId));
    }

    @GetMapping("/date/{date}/{telegramChatId}")
    ResponseEntity<List<TransactionRecordDTO>> getTransactionRecordsByTelegramChatIdAndDate(@NotBlank @PathVariable String telegramChatId,
                                                                                            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date date) {
        dateValidator.validate(date);
        return ResponseEntity.ofNullable(transactionRecordFacade.getTransactionRecordsByTelegramChatIdAndDate(telegramChatId, date));
    }

    @GetMapping("/date/{date}/summary/{telegramChatId}")
    ResponseEntity<TransactionSummaryDTO> getTransactionSummaryForDateAndTelegramChatId(@NotBlank @PathVariable String telegramChatId,
                                                                                        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date date) {
        dateValidator.validate(date);
        return ResponseEntity.ofNullable(transactionRecordFacade.getTransactionSummaryForDateAndTelegramChatId(telegramChatId, date));
    }

    @PostMapping
    ResponseEntity<TransactionRecordDTO> saveTransactionRecord(@RequestBody @Nonnull TransactionRecordDTO transactionRecord) {
        transactionRecordValidator.validateForSave(transactionRecord);
        return ResponseEntity.ofNullable(transactionRecordFacade.saveTransactionRecord(transactionRecord));
    }

    @DeleteMapping("/id/{transactionRecordId}")
    @ResponseStatus(HttpStatus.OK)
    void deleteTransactionRecordById(@PathVariable @NotBlank String transactionRecordId) {
        transactionRecordFacade.deleteTransactionRecordById(transactionRecordId);
    }

    @DeleteMapping("/telegram/chat/id/{telegramChatId}")
    @ResponseStatus(HttpStatus.OK)
    void deleteTransactionRecordsByTelegramChatId(@PathVariable @NotBlank String telegramChatId) {
        transactionRecordFacade.deleteTransactionRecordsByTelegramChatId(telegramChatId);
    }

    @PutMapping
    ResponseEntity<TransactionRecordDTO> updateTransactionRecord(@RequestBody @Nonnull TransactionRecordDTO transactionRecord) {
        transactionRecordValidator.validateForUpdate(transactionRecord);
        return ResponseEntity.ofNullable(transactionRecordFacade.updateTransactionRecord(transactionRecord));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Exception thrown during execution: {}", e.getMessage());
        return ResponseEntity.notFound().build();
    }
}
