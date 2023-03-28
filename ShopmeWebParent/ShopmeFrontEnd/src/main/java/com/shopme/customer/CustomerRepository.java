package com.shopme.customer;

import com.shopme.common.entity.AuthenticationType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
	@Query("SELECT c FROM Customer c WHERE c.email = ?1")
	Customer findByEmail(String email);
	
	@Query("SELECT c FROM Customer c WHERE c.verificationCode = ?1")
	Customer findByVerificationCode(String code);
	
	Boolean existsByEmail(String email);
	
	@Query("SELECT c.email FROM Customer c WHERE c.id = ?1")
	String findEmailById(Integer id);

	Boolean existsByVerificationCode(String code);

	@Query("UPDATE Customer c SET c.authenticationType = ?1 WHERE c.id = ?2")
	@Modifying
	void updateAuthenticationType(AuthenticationType type, Integer customerId);

	@Query("SELECT c FROM Customer c WHERE c.resetPasswordToken = ?1")
	Customer findCustomerByResetPasswordToken(String resetToken);
}
