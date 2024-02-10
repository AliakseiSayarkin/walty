package com.walty.currency.web.api.v1;

import com.walty.currency.facade.CurrencyExchangeFacade;
import com.walty.currency.web.dto.CurrencyDTO;
import com.walty.currency.web.dto.CurrencyValuesDTO;
import com.walty.currency.web.validator.ValidCurrency;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/v1/currency")
@AllArgsConstructor
public class CurrencyExchangeController {

    private final CurrencyExchangeFacade currencyExchangeFacade;

    @GetMapping("/exchange/{currencyToBuyCode}")
    public ResponseEntity<CurrencyDTO> exchangeCurrency(@RequestBody @Valid CurrencyDTO currencyToSell,
                                                       @PathVariable @ValidCurrency String currencyToBuyCode) {
        return ResponseEntity.of(currencyExchangeFacade.exchangeCurrency(currencyToSell, currencyToBuyCode));
    }

    @GetMapping("/values")
    public ResponseEntity<CurrencyValuesDTO> createCurrencyValues(@RequestBody @Valid CurrencyDTO currencyToSell) {
        return ResponseEntity.of(currencyExchangeFacade.createCurrencyValues(currencyToSell));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Exception thrown during execution: {}", e.getMessage());
        return ResponseEntity.notFound().build();
    }
}

