package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest
@Rollback(false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class BrandRepositoryTest {
	
	@Autowired
	public BrandRepository brandRepository;
	
	@Test
	public void addBrandsTest() {
		Category laptop = new Category(6);
		Brand acer = new Brand("Acer");
		acer.getCategories().add(laptop);
		
		Category cellPhonesAndAccessories = new Category(4);
		Category tablets = new Category(7);
		Category memory = new Category(29);
		Category hardDrive = new Category(24);
		
		Brand apple = new Brand("Apple");
		Brand samsung = new Brand("Samsung");
		
		apple.getCategories().addAll(List.of(cellPhonesAndAccessories, tablets));
		samsung.getCategories().addAll(List.of(hardDrive, memory));
		
		brandRepository.saveAll(List.of(apple,samsung));
		
		assertThat(brandRepository.findAll().size()).isGreaterThan(0);
	}
	
	
}
