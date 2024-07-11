package com.topia.service;

import java.time.LocalDate;
import java.util.List;

import com.topia.model.ExchangeRate;

public interface ExchangeRateService {

	public ExchangeRate getExchangeRate(String targetCurrency, LocalDate date);
	
	public List<ExchangeRate> getLastThreeExchangeRates(String targetCurrency);
	
}
