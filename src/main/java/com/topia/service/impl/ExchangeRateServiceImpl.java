package com.topia.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.topia.model.ExchangeRate;
import com.topia.repository.ExchangeRateRepository;
import com.topia.response.ExchangeRateResponse;
import com.topia.service.ExchangeRateService;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRateServiceImpl.class);

	@Autowired
	private ExchangeRateRepository exchangeRateRepository;

	@Value("${exchange.rate.api.url}")
	private String apiUrl;

	@Autowired
	RestTemplate restTemplate;;

	@Override
	public ExchangeRate getExchangeRate(String targetCurrency, LocalDate date) {
		
		Optional<ExchangeRate> latestRate = exchangeRateRepository
				.findFirstByTargetCurrencyAndDateOrderByDateDesc(targetCurrency, date);
		if (latestRate.isPresent()) {
			return latestRate.get();
		} else {
			return fetchAndSaveExchangeRate(targetCurrency, date);
		}
	}

	private ExchangeRate fetchAndSaveExchangeRate(String targetCurrency, LocalDate date) {
		String dateOrLatest = "latest";
		if(date!=null) {
			dateOrLatest = String.valueOf(date);
		}
		String url = String.format("%s/%s?to=%s", apiUrl, dateOrLatest, targetCurrency);
		try {
			ResponseEntity<ExchangeRateResponse> response = restTemplate.getForEntity(url, ExchangeRateResponse.class);
			if (response != null && response.getBody() != null && !response.getBody().validateNullCheck()) {
				ExchangeRateResponse exchangeRateResponse = response.getBody();
				ExchangeRate exchangeRate = new ExchangeRate();
				exchangeRate.setDate(exchangeRateResponse.getDate());
				exchangeRate.setSourceCurrency("USD");
				exchangeRate.setTargetCurrency(targetCurrency);
				exchangeRate.setRate(exchangeRateResponse.getRates().get(targetCurrency));
				return exchangeRateRepository.save(exchangeRate);
			}
		} catch (HttpClientErrorException ex) {
			LOGGER.error("Error while fetching data from external API", ex.getMessage());
			throw new HttpClientErrorException(ex.getStatusCode(), "Failed to fetch exchange rate from external API");
		}
		return null;
	}

	@Override
	public List<ExchangeRate> getLastThreeExchangeRates(String targetCurrency) {
		List<ExchangeRate> rates = exchangeRateRepository.findTop3ByTargetCurrencyOrderByDateDesc(targetCurrency);
		if (rates.isEmpty()) {
			throw new NoSuchElementException("Exchange rates for " + targetCurrency + " not found");
		}
		return rates;
	}
}
