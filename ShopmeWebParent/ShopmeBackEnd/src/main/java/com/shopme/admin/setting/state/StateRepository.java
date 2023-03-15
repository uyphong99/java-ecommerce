package com.shopme.admin.setting.state;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

public interface StateRepository extends CrudRepository<State, Integer> {
	
	@Query("SELECT s FROM State s WHERE s.country = ?1")
	public List<State> findStatesByCountry(Country country);
}
