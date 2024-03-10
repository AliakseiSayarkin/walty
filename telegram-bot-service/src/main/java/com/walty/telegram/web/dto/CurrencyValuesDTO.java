package com.walty.telegram.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyValuesDTO {

    @JsonProperty("USD")
    private double usd;
    @JsonProperty("EUR")
    private double eur;
    @JsonProperty("BYN")
    private double byn;
    @JsonProperty("RUB")
    private double rub;
}
