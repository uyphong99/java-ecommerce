package com.shopme.admin.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {
	
	@Autowired
	private UserService uService;
	
	@PostMapping("/users/check_email")
	public String checkDuplicateEmail(@Param("id") Integer id, @Param("email") String email) {
		return uService.isDuplicatedEmail(id, email)  ? "Duplicated" : "OK";
	}
	
	@PostMapping("/users/check_duplicated")
	public String isExistedEmail(@Param("id") Integer id, @Param("email") String email) {
		if (id == null && uService.isExistedEmail(email)) {
			return "Duplicated";
		}
		
		return "OK";
	}
}
