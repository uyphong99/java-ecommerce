package com.shopme.customer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
	@Query("SELECT c FROM Customer c WHERE c.email = ?1")
	public Customer findByEmail(String email);
	
	@Query("SELECT c FROM Customer c WHERE c.verificationCode = ?1")
	public Customer findByVerificationCode(String code);
	
	public Boolean existsByEmail(String email);
	
	@Query("SELECT c.email FROM Customer c WHERE c.id = ?1")
	public String findEmailById(Integer id);

	public Boolean existsByVerificationCode(String code);
}
