package com.shopme.customer;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.setting.country.CountryRepository;

@Service
public class CustomerService {
	private CustomerRepository repository;
	
	public CountryRepository countryRepository;
	
	public List<Country> listAllCountry() {
		return countryRepository.listAllCountry();
	}
	
	public Boolean existsByEmail(String email) {
		return repository.existsByEmail(email);
	}
	
	public Customer findByEmail(String email) {
		return repository.findByEmail(email);
	}
	
	
	public Boolean isUniqueEmail(Customer customer) {
		String email = customer.getEmail();
		
		if(customer.getId() == null) {
			if (existsByEmail(email)) {
				return false;
			}
		} else {
			String oldEmail = repository.findEmailById(customer.getId());
			if (!email.equals(oldEmail)) {
				return false;
			}
		}
		
		return true;
	}
}
