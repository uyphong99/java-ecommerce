package com.shopme.admin.setting.state;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@Service
public class StateService {
	private StateRepository repository;
	
	public StateService(@Autowired StateRepository repository) {
		this.repository = repository;
	}
	
	public List<State> findStatesByCountry(Country country) {
		return repository.findStatesByCountry(country);
	}
}
