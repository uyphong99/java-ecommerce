package com.shopme.admin.product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.product.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	boolean existsByName(String name);
	
	Product findByName(String name);
	
	@Query("SELECT p FROM Product p WHERE CONCAT(p.name, p.brand, p.category) LIKE %?1%")
	List<Product> findProductsByKeyword(String keyword);

	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
	Page<Product> findPageByKeyword(String keyword, Pageable pageable);
}
