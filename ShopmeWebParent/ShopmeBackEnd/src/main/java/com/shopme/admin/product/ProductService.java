package com.shopme.admin.product;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Product;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	public List<Product> listAll() {
		return repository.findAll();
	}
	
	public Product save(Product product) {
		
		if (product.getId() == null) {
			product.setCreatedTime(LocalDateTime.now());
		}
		
		if (product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getName().replace(' ', '-');
			product.setAlias(defaultAlias);
		} else {
			product.setAlias(product.getAlias().replace(' ', '-'));
		}
		
		product.setUpdatedTime(LocalDateTime.now());
		
		return repository.save(product);
	}
	
	public boolean checkUnique(String productName, Integer id) {
		
		if(repository.existsByName(productName) && id == null) {
			return false;
		}
		
		if( !repository.existsByName(productName) && id == null) {
			return true;
		}
		
		Product product = repository.findById(id).get();
		String oldName = product.getName();
		
		if (!oldName.equals(productName)) {
			if (repository.existsByName(productName)) return false;
		}
		
		return true;
	}
	
	public Product findById(Integer id) {
		return repository.findById(id).get();
	}
	
	public void deleteProductById(Integer id) {
		repository.deleteById(id);
	}
}
