package com.walty.telegram.service.impl;

import com.walty.telegram.service.ParsingService;
import com.walty.telegram.config.Currency;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Service
public class DefaultParsingService implements ParsingService {

    private static final char DASH = '/';
    private static final char SPACE = ' ';
    private static final char DOT = '.';
    private static final String DESCRIPTION_ATTRIBUTE = "-d";

    @Override
    public Optional<String> getCommand(String input) {
        if (Objects.isNull(input) || input.isBlank()) {
            return Optional.empty();
        }

        var formattedInput = input.trim().toLowerCase();

        if (!startsWithDash(formattedInput)) {
            return Optional.empty();
        }

        var startIndex = 1;
        var endIndex = formattedInput.indexOf(SPACE, startIndex);

        if (isOutOfBounds(startIndex, endIndex, formattedInput)) {
            return Optional.empty();
        }

        return endIndex == -1 ? Optional.of(formattedInput.substring(startIndex)) :
                                Optional.of(formattedInput.substring(startIndex, endIndex));
    }

    private boolean startsWithDash(String input) {
        return input.charAt(0) == DASH;
    }

    private boolean isOutOfBounds(int startIndex, int endIndex, String input) {
        return startIndex >= input.length() || endIndex >= input.length();
    }

    @Override
    public Optional<Double> getValue(String input) {
        if (Objects.isNull(input) || input.isBlank()) {
            return Optional.empty();
        }

        var formattedInput = input.trim().toLowerCase();
        var firstValue = getFirstValue(formattedInput);

        return firstValue.isEmpty() ? Optional.empty() : Optional.of(Double.parseDouble(firstValue));
    }

    private String getFirstValue(String input) {
        var builder = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (Character.isDigit(c) || DOT == c) {
                builder.append(c);
            } else if (!builder.isEmpty()) {
                break;
            }
        }

        return builder.toString();
    }

    @Override
    public Optional<Currency> getCurrency(String input) {
        if (Objects.isNull(input) || input.isBlank()) {
            return Optional.empty();
        }

        var formattedInput = input.trim().toLowerCase();
        return Arrays.stream(Currency.values())
                .filter(currency -> formattedInput.contains(currency.toString().toLowerCase()))
                .findFirst();
    }

    @Override
    public Optional<String> getDescription(String input) {
        var index = input.indexOf(DESCRIPTION_ATTRIBUTE);
        if (index == -1) {
            return Optional.empty();
        }

        return Optional.of(input.substring(index + DESCRIPTION_ATTRIBUTE.length()).trim());
    }

    @Override
    public Optional<Integer> getDescriptionBeginIndex(String input) {
        var index = input.indexOf(DESCRIPTION_ATTRIBUTE);
        return index == -1 ? Optional.empty() : Optional.of(index);
    }

    @Override
    public Optional<String> getTransactionRecordId(String input) {
        var index = input.trim().indexOf(SPACE);
        return index == -1 ? Optional.empty() : Optional.of(input.substring(index + 1));
    }
}
