package com.shopme.customer;

import java.util.List;
import java.util.Random;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.setting.country.CountryRepository;

@Service
@AllArgsConstructor
public class CustomerService {
	private CustomerRepository repository;
	
	private CountryRepository countryRepository;

	private PasswordEncoder encoder;
	
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
		String encodedPassword = encoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
		customer.setEnabled(true);

		String verificationCode = generateRandomString();
		customer.setVerificationCode(verificationCode);

		return repository.save(customer);
	}

	public String generateRandomString() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		int length = 64;
		Random random = new Random();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < length; i++) {
			int index = random.nextInt(characters.length());
			char randomChar = characters.charAt(index);
			sb.append(randomChar);
		}

		return sb.toString();
	}

	public boolean existsByVerificationCode(String code) {
		return repository.existsByVerificationCode(code);
	}

	public Customer findByVerificationCode(String code) {
		return repository.findByVerificationCode(code);
	}
}
