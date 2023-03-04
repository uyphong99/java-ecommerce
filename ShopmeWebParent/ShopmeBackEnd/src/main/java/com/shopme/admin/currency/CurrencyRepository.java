package com.shopme.admin.currency;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Integer>{
	
	@Query("SELECT c FROM Currency c ORDER BY c.id")
	public List<Currency> findAllOrderById();
}
