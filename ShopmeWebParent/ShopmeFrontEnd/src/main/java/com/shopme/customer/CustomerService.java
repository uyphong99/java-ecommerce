package com.shopme.customer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.exception.CustomerNotFoundException;
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
		if (customer.getPassword() != null) {
			String encodedPassword = encoder.encode(customer.getPassword());
			customer.setPassword(encodedPassword);
		}

		customer.setEnabled(false);

		String verificationCode = generateRandomString(64);
		customer.setVerificationCode(verificationCode);

		return repository.save(customer);
	}

	public String generateRandomString(Integer length) {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
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

	public void updateCustomerAuthentication(AuthenticationType type, Integer customerId) {
		repository.updateAuthenticationType(type, customerId);
	}

	public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode,
											 AuthenticationType authType) {
		String[] customerName = name.split(" ");
		String firstName = customerName[0];
		String lastName = customerName[customerName.length - 1];
		Customer customer = Customer.builder()
				.email(email)
				.firstName(firstName)
				.lastName(lastName)
				.country(countryRepository.findByCode(countryCode))
				.enabled(true)
				.authenticationType(authType)
				.createTime(LocalDateTime.now())
				.build();

		repository.save(customer);
	}

	public void updateCustomerAuthenticationType(Customer customer, AuthenticationType authenticationType) {
		customer.setAuthenticationType(authenticationType);
	}

	public void resetVerifyCode(String email) {
		Customer customer = repository.findByEmail(email);
		String newCode = generateRandomString(64);

		customer.setVerificationCode(newCode);
		save(customer);
	}

	public void generateResetPasswordToken(String email) {
		Customer customer = repository.findByEmail(email);
		String resetToken = generateRandomString(30);

		customer.setResetPasswordToken(resetToken);
		save(customer);
	}

	public Customer findByToken(String token) {
		return repository.findCustomerByResetPasswordToken(token);
	}

	public Customer resetPassword(String password, String token) throws CustomerNotFoundException {
		String encodedPassword = encoder.encode(password);
		Customer customer = findByToken(token);

		customer.setPassword(encodedPassword);

		repository.save(customer);

		return customer;
	}
}
