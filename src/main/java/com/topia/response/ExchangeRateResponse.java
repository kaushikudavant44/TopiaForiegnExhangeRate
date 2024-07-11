package com.topia.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class ExchangeRateResponse {
	
	private LocalDate date;
	private Map<String, BigDecimal> rates;
	
	 public boolean validateNullCheck() {
	        return date == null && (rates == null || rates.isEmpty());
	    }

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Map<String, BigDecimal> getRates() {
		return rates;
	}

	public void setRates(Map<String, BigDecimal> rates) {
		this.rates = rates;
	}

}
