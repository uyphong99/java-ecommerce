package com.shopme.admin.setting.country;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.Country;

@Service
public class CountryService {
	
	private CountryRepository repository;
	
	public List<Country> findAllCountry() {
		return (List<Country>) repository.findAll();
	}
	
	
}
