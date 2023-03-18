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
	
	/**
	 * If id != null and email equals old email return true.
	 * If id == null and not exists duplicated email return true.
	 *
	 */
	public boolean isUniqueEmail(Customer customer) {
		String email = customer.getEmail();
		Integer id = customer.getId();

		if (id == null) {
			return !repository.existsByEmail(email);
		}

		String oldEmail = repository.findEmailById(id);
		return email.equals(oldEmail) || !repository.existsByEmail(email);
	}

	public Customer save(Customer customer) {
		return repository.save(customer);
	}
}
