package com.shopme.admin.setting.country;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Country;

@Service
public class CountryService {
	
	@Autowired
	private CountryRepository repository;
	
	public List<Country> findAllCountry() {
		return (List<Country>) repository.findAll();
	}
	
	public Country findById(Integer id) {
		return repository.findById(id).get();
	}
}
