package com.topia.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.topia.model.ExchangeRate;
import com.topia.service.ExchangeRateService;

@RestController
@RequestMapping("/fx")
public class ExchangeRateController {

    @Autowired
    private ExchangeRateService service;

    @GetMapping
    public ResponseEntity<ExchangeRate> getExchangeRate(@Validated @RequestParam String targetCurrency,@Validated @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        ExchangeRate exchangeRate = service.getExchangeRate(targetCurrency, date);
        if(exchangeRate==null) {
        	throw new NoSuchElementException("Exchange rates for " + targetCurrency + " not found");
        }
        return ResponseEntity.ok(exchangeRate);
    }

    @GetMapping("/{targetCurrency}")
    public ResponseEntity<List<ExchangeRate>> getLastThreeExchangeRates(@PathVariable String targetCurrency) {
        List<ExchangeRate> exchangeRates = service.getLastThreeExchangeRates(targetCurrency);
        return ResponseEntity.ok(exchangeRates);
    }
    
}
