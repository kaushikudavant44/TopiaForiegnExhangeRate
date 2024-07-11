package com.topia.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.topia.model.ExchangeRate;
import com.topia.service.ExchangeRateService;

class ExchangeRateControllerTest {

	@Mock
	private ExchangeRateService exchangeRateService;

	@InjectMocks
	private ExchangeRateController exchangeRateController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetExchangeRate_Success() {
	  
	    String targetCurrency = "JPY";
	    ExchangeRate mockExchangeRate = new ExchangeRate();
	    mockExchangeRate.setSourceCurrency("USD");
	    
	    mockExchangeRate.setTargetCurrency(targetCurrency);
	    mockExchangeRate.setRate(new BigDecimal("0.9012")); // Using string constructor for BigDecimal
	    LocalDate date = LocalDate.parse("2024-07-01");
	    mockExchangeRate.setDate(date);
	    when(exchangeRateService.getExchangeRate(targetCurrency, date))
	            .thenReturn(mockExchangeRate);

	    ResponseEntity<ExchangeRate> response = exchangeRateController.getExchangeRate(targetCurrency, date);

	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertNotNull(response.getBody());

	    ExchangeRate returnedRate = response.getBody();
	    assertEquals("USD", returnedRate.getSourceCurrency());
	    assertEquals(targetCurrency, returnedRate.getTargetCurrency());
	    assertEquals(new BigDecimal("0.9012"), returnedRate.getRate());
	}


	@Test
	public void testGetExchangeRate_NotFound() {
		// Mock data
		String targetCurrency = "XYZ";

		// Mock service method returning null
		when(exchangeRateService.getExchangeRate(eq(targetCurrency), any(LocalDate.class))).thenReturn(null);

		// Call controller method
		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
			exchangeRateController.getExchangeRate(targetCurrency, null);
		});

		// Verify exception message
		assertTrue(exception.getMessage().contains("Exchange rates for " + targetCurrency + " not found"));
	}

	@Test
	public void testGetLastThreeExchangeRates_Success() {
		// Mock data
		String targetCurrency = "EUR";
		ExchangeRate rate1 = getExchangeRate(targetCurrency, new BigDecimal(0.9087));
		ExchangeRate rate2 = getExchangeRate(targetCurrency, new BigDecimal(0.9056));
		ExchangeRate rate3 = getExchangeRate(targetCurrency, new BigDecimal(0.9083));
		List<ExchangeRate> mockRates = Arrays.asList(rate1, rate2, rate3);

		// Mock service method
		when(exchangeRateService.getLastThreeExchangeRates(targetCurrency)).thenReturn(mockRates);

		// Call controller method
		ResponseEntity<List<ExchangeRate>> response = exchangeRateController.getLastThreeExchangeRates(targetCurrency);

		// Verify response
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(mockRates, response.getBody());
	}

	private ExchangeRate getExchangeRate(String targetCurrency, BigDecimal price) {
		ExchangeRate exchangeRate = new ExchangeRate();
		exchangeRate.setSourceCurrency("USD");
		exchangeRate.setTargetCurrency(targetCurrency);
		exchangeRate.setRate(price);
		return exchangeRate;
	}
}
