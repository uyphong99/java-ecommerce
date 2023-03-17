package com.shopme.setting.country;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Country;

public interface CountryRepository extends CrudRepository<Country, Integer>{
	
	@Query("SELECT c FROM Country c")
	public List<Country> listAllCountry();
}
