package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class ProductServiceTests {
	
	@Autowired
	private ProductService service;
	
	@Test
	public void testCheckUniqueProduct() {
		assertThat(service.checkUnique("Thinkpad Lenovo t470s", null)).isFalse();
	}
}
