package com.walty.currency.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class RoundingUtil {

    @Value("${currency.rounding-scale}")
    private int roundingScale;

    public double round(double value) {
        var roundedValue = BigDecimal.valueOf(value).setScale(roundingScale, RoundingMode.HALF_EVEN);
        return roundedValue.doubleValue();
    }
}
