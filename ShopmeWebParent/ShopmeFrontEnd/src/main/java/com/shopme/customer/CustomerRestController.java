package com.shopme.customer;

import com.shopme.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {
	@Autowired
	private CustomerService service;

	@PostMapping("/customers/check_unique_email")
	public Boolean checkUniqueEmail(String email) {

		return !service.existsByEmail(email);
	}
}
