package com.shopme.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {
	@Autowired
	private CustomerService service;
	
	public Boolean checkDuplicateEmail(String email) {
		return true;
	}
}
