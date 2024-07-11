package com.topia.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.topia.model.ExchangeRate;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
	List<ExchangeRate> findTop3ByTargetCurrencyOrderByDateDesc(String targetCurrency);

	Optional<ExchangeRate> findFirstByTargetCurrencyAndDateOrderByDateDesc(String targetCurrency, LocalDate date);
}
