package com.shopme.admin.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	public boolean existsByName(String name);
	
	public Product findByName(String name);
	
	@Query("SELECT p FROM Product p WHERE CONCAT(p.name, p.brand, p.category) LIKE %?1%")
	public List<Product> findProductsByKeyword(String keyword);
}
