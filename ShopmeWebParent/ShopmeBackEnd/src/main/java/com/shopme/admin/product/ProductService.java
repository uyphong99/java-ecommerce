package com.shopme.admin.product;

import java.time.LocalDateTime;
import java.util.List;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Customer;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.product.Product;

@Service
@AllArgsConstructor
public class ProductService {
	

	private ProductRepository repository;

	public static final Integer PRODUCTS_PER_PAGE = 10;
	
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
	
	public List<Product> findByKeyword(String keyword) {
		return repository.findProductsByKeyword(keyword);
	}

	public void searchProducts(int pageNum, PagingAndSortingHelper helper) {
		Pageable pageable = helper.createPageable(PRODUCTS_PER_PAGE, pageNum);
		String keyword = helper.getKeyword();
		Page<Product> page = repository.findPageByKeyword(keyword, pageable);
		helper.updateModelAttributes(pageNum, page);
	}

	public Page<Product> listProducts(int pageNumber, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNumber - 1, PRODUCTS_PER_PAGE, sort);

		if (keyword == null) {
			return repository.findAll(pageable);
		}

		return repository.findPageByKeyword(keyword, pageable);
	}
}
