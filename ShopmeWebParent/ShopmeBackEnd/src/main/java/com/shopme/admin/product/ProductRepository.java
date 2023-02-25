package com.shopme.admin.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopme.common.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	public boolean existsByName(String name);
	
	public Product findByName(String name);
}
