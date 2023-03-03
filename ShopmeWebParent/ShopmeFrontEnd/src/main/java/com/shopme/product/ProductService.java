package com.shopme.product;


import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	public static final int PRODUCTS_PER_PAGE = 10;
	
	public Page<Product> listByCategory(Set<Category> subCategories, Integer pageNum) {
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);
		
		return repository.findAllByCategory(subCategories, pageable);
	}
	
	public Product findProductByAlias(String alias) {
		return repository.findByAlias(alias);
	}
	
	public Page<Product> findProductByKeyword(String keyword, Integer pageNum) {
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);
		
		return repository.searchProductsByKeyword(keyword, pageable);
	}
}
