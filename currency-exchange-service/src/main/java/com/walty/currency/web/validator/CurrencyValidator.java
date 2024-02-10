package com.walty.currency.web.validator;

import com.walty.currency.Currency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

    private Map<String, Currency> currencyToCode;

    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {
        return currencyToCode.containsKey(code);
    }

    @Override
    public void initialize(ValidCurrency constraintAnnotation) {
        currencyToCode = Stream.of(Currency.values()).collect(Collectors.toMap(Enum::toString, Function.identity()));
    }
}