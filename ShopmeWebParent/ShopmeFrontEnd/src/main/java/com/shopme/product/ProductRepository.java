package com.shopme.product;


import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	@Query("SELECT p FROM Product p WHERE p.category IN ?1 AND p.enabled = true")
	public Page<Product> findAllByCategory(Set<Category> subCategories, Pageable pageable);
	
	public Product findByAlias(String alias);
	
	@Query(value = "SELECT * FROM products WHERE enabled = true "
			+ "AND MATCH(name, short_description, full_description) AGAINST (?1)", 
			nativeQuery = true)
	public Page<Product> searchProductsByKeyword(String keyword, Pageable pageable);
	
}
