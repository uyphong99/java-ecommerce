package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {
	
	@Autowired
	private CategoryRepository repository;

	
	@Test
	public void testFirstCategoryChild() {
		Category category = repository.findById(1).get();
		Set<Category> children = category.getChildren();
		
		assertThat(children.size()).isGreaterThan(2);
	}
	
	@Test
	public void testRootCategory() {
		Integer sizeRootCategories = (int) repository.findAll().stream().filter(x -> x.getParent()==null).count();
		
		assertThat(sizeRootCategories).isGreaterThan(2);
	}
}
