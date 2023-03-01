package com.shopme.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	public List<Category> listNoChildrenCategories() {
		List<Category> children = repository.findAllChildrenCategories();
		return children;
	}
}
