package com.shopme.admin.setting.state;

import java.util.List;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.setting.country.CountryService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@RestController
public class StateRestController {
	
	@Autowired
	private StateService service;
	
	@Autowired
	private CountryService countryService;
	
	@GetMapping("/states/list_by_country/{id}")
	public List<StateDTO> listAllByCountries(@PathVariable("id") Integer id) {
		Country country = countryService.findById(id);
		List<State> states = service.findStatesByCountry(country);
		
		return states.stream().map(state -> new StateDTO(state)).toList();
	}
	
	@PostMapping("/states/save")
	public String saveState(@RequestBody State state) {
		State savedState = service.save(state);
		
		return String.valueOf(savedState.getId());
	}
	
	@DeleteMapping("/states/delete/{id}")
	public void deleteState(@PathVariable("id") Integer id) {
		State state = service.findById(id);
		service.deleteState(state);
	}
}
