package com.walty.currency.service.integration.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walty.currency.service.integration.dto.CurrencyExchangeRateDTO;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.Objects.nonNull;

@Slf4j
@Component
@AllArgsConstructor
public final class CurrencyRatesJsonConverter implements Converter<String, Optional<CurrencyExchangeRateDTO>> {

    private static final String RESULT_JSON_FIELD = "result";
    private static final String BASE_CODE_JSON_FIELD = "base_code";
    private static final String RATES_JSON_FIELD = "rates";

    private static final String RESULT_SUCCESS = "success";

    private final ObjectMapper mapper;

    @Override
    public Optional<CurrencyExchangeRateDTO> convert(@Nonnull String json) {
        try {
            JsonNode jsonNode = mapper.readTree(json);

            if (!isValid(jsonNode)) {
                return Optional.empty();
            }

            return createCurrency(jsonNode);
        } catch (JsonProcessingException e) {
            log.error("Error during parsing of a JSON: {}", json);
            return Optional.empty();
        }
    }

    private boolean isValid(JsonNode json) {
        return isSuccessfulAPICall(json) &&
                json.has(BASE_CODE_JSON_FIELD) &&
                json.has(RATES_JSON_FIELD);
    }

    private boolean isSuccessfulAPICall(JsonNode jsonNode) {
        JsonNode resultField = jsonNode.get(RESULT_JSON_FIELD);
        return nonNull(resultField) && RESULT_SUCCESS.equals(resultField.asText());
    }

    private Optional<CurrencyExchangeRateDTO> createCurrency(JsonNode jsonNode) throws JsonProcessingException {
        var currency = mapper.readValue(jsonNode.get(RATES_JSON_FIELD).toString(), CurrencyExchangeRateDTO.class);
        currency.setCode(jsonNode.get(BASE_CODE_JSON_FIELD).asText());

        return Optional.of(currency);
    }
}
