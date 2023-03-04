package com.shopme.admin.setting;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.admin.currency.CurrencyRepository;
import com.shopme.common.entity.Currency;

@Service
public class CurrencyService {
	private CurrencyRepository repository;
	
	public CurrencyService(CurrencyRepository repository) {
		this.repository = repository;
	}
	
	public List<Currency> findAllCurrencyOrderById() {
		return repository.findAllOrderById();
	}
}
