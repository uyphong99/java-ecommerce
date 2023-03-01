package com.shopme.category;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CategoryServiceTests {
	@Autowired
	private CategoryService service;
	
	@Test
	public void listChildrenCategoryTest() {
		assertThat(service.listNoChildrenCategories().size())
			.isEqualTo(25);
	}
}
