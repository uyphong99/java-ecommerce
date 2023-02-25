package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LayersSpringBootTests {
	
	@Autowired
	UserRestController userRestController;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService service;
	
	@Test
	void checkDuplicatedEmailTest() {
		assertThat(userRestController.checkDuplicateEmail(null, "uyphong99@gmail.com").equals("Duplicated")).isTrue();
		assertThat(userRestController.checkDuplicateEmail(9, "uyphong99@gmail.com").equals("Duplicated")).isFalse();
	}
	
	
	@Test
	void existedEmail_uyphong99_test() {
		assertThat(userRepository.existsByEmail("uyphong99@gmail.com")).isTrue();
	}
	
	@Test
	void isDuplicatedEmailTest() {
		assertThat(service.isDuplicatedEmail(null, "uyphong99@gmail.com")).isTrue();
	}
}
