package com.shopme.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.shopme.common.entity.AuthenticationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shopme.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CustomerRepositoryTests {
	
	@Autowired
	private CustomerRepository repository;
	
	@Test
	void testRepository() {
		List<Customer> customers = (List<Customer>) repository.findAll();
		
		
		assertThat(customers.size()).isGreaterThan(0);
	}
	
	@Test
	void testCreateCustomer() {
		Customer customer = new Customer();
		customer.setFirstName("A");
		customer.setLastName("B");
		customer.setEmail("A@mail.com");
		customer.setPassword("fdsfsdfsdf");
		
		repository.save(customer);
		
		assertThat(repository.count()).isGreaterThan(2);
	}
	
	@Test
	void findByEmailTest() {
		String email = "uyphong99@gmail.com";
		Customer customer = repository.findByEmail(email);
		assertThat(customer.getId()).isNull();
	}
	
	@Test
	void findByVerificationCode() {
		Customer customer = repository.findByVerificationCode("455455");
		
		assertThat(customer).isNull();
	}
	
	@Test
	void existByEmailTest() {
		Boolean exist = repository.existsByEmail("uyphong99@gmail.com");
		
		assertThat(exist).isFalse();
	}

	@Test
	void customerAuthenticationTest() {
		Integer id = 1;
		repository.updateAuthenticationType(AuthenticationType.GOOGLE, id);
		Customer customer = repository.findById(id).get();

		assertThat(customer.getAuthenticationType().equals(AuthenticationType.GOOGLE)).isTrue();
	}
}
