package com.walty.currency.service.model;

import com.walty.currency.config.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyModel {

    private Currency code;
    private double value;
}
