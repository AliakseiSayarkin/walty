package com.walty.telegram.service;

import com.walty.telegram.config.Currency;

import java.util.Optional;

public interface ParsingService {

    Optional<String> getCommand(String input);
    Optional<Double> getValue(String input);
    Optional<Currency> getCurrency(String input);
    Optional<String> getDescription(String input);
    Optional<Integer> getDescriptionBeginIndex(String input);
    Optional<String> getTransactionRecordId(String input);
}
