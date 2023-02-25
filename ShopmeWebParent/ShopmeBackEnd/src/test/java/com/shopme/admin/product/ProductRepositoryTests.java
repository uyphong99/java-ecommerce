package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {

		@Autowired
		private ProductRepository repository;
		
		@Autowired
		private TestEntityManager entityManager;
		
		@Test
		public void testCreateProduct() {
			Brand brand = entityManager.find(Brand.class,38);
			Category cellphones = entityManager.find(Category.class, 6);
			
			Product product = new ProductBuilder().setName("Dell Inspirion 3000")
									.setShortDescription("Short Description")
									.setFullDescription("This is full description")
									.setBrand(brand)
									.setCategory(cellphones)
									.setPrice(600)
									.setCreatedTime(LocalDateTime.now())
									.setUpdatedTime(LocalDateTime.now())
									.setAlias("dell_ins_3000")
									.build();
			
			Product savedProduct = repository.save(product);
			
			assertThat(savedProduct).isNotNull();
			assertThat(savedProduct.getId()).isGreaterThan(0);
		}
		
		@Test
		public void testProductInDb() {
			Product product = repository.findById(1).get();
			
			assertThat(product).isNotNull();
		}
		
		@Test
		public void testSaveProductWithImages() {
			Integer productId = 1;
			Product product = repository.findById(productId).get();
			
			product.setMainImage("main image.jpg");
			product.addExtraImage("extra image 1.png");
			product.addExtraImage("extra_image_2.png");
			product.addExtraImage("extra-image3.png");
			
			Product savedProduct = repository.save(product);
			
			assertThat(savedProduct.getImages().size()).isEqualTo(3);
		}
}
