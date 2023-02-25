package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
	
	@Test
	public void testEncoderPassword() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String rawPassword = "nam2020";
		String encodedPassword = passwordEncoder.encode(rawPassword);
		
		boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
		
		System.out.println(encodedPassword);
		
		assertThat(matches).isTrue();
	}
}
