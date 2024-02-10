package com.walty.currency.web.dto;

import com.walty.currency.web.validator.ValidCurrency;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDTO {

    @ValidCurrency
    private String code;

    @DecimalMin(value = "0", inclusive = false)
    private double value;
}
