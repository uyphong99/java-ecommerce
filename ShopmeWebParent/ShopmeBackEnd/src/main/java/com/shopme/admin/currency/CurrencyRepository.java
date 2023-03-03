package com.shopme.admin.currency;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopme.common.entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Integer>{

}
