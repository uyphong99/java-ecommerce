package com.shopme.admin.setting.country;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Country;

public interface CountryRepository extends CrudRepository<Country, Integer>{
	
	@Query("SELECT c FROM Country c ORDER BY c.name ASC")
	public List<Country> findAllAsc();
}
