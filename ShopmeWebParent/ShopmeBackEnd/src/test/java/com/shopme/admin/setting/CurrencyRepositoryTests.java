package com.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.currency.CurrencyRepository;
import com.shopme.common.entity.Currency;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CurrencyRepositoryTests {
	
	private CurrencyRepository repository;
	
	public CurrencyRepositoryTests(@Autowired CurrencyRepository repository) {
		this.repository = repository;
	}
	
	@Test
	public void testCreateCurrency() {
		List<Currency> listCurrencies = List.of(
				new Currency("United States Dollar", "$", "USD"),
				new Currency("British Pound", "£", "GPB"),
				new Currency("Japanese Yen", "¥", "JPY"),
				new Currency("Euro", "€", "EUR"),
				new Currency("Russian Ruble", "₽", "RUB"),
				new Currency("South Korean Won", "₩", "KRW"),
				new Currency("Chinese Yuan", "¥", "CNY"),
				new Currency("Brazilian Real", "R$", "BRL"),
				new Currency("Australian Dollar", "$", "AUD"),
				new Currency("Canadian Dollar", "$", "CAD"),
				new Currency("Vietnamese đồng ", "₫", "VND"),
				new Currency("Indian Rupee", "₹", "INR")
			);
			
		repository.saveAll(listCurrencies);
		
		assertThat(repository.count()).isGreaterThan(0);
	}
	
	@Test
	public void testFindAllCurrencies() {
		List<Currency> currencies = repository.findAllOrderById();
		
		assertThat(currencies.size()).isGreaterThan(10);
		assertThat(currencies.get(0).getId()).isEqualTo(2);
	}
}
