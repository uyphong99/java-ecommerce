package com.shopme.admin.category;

import java.util.List;

import org.hibernate.query.NativeQuery.ReturnableResultNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

@Service
public class CategoryService {
	
	public static final int CATEGORY_PER_PAGE = 10;
	
	@Autowired
	CategoryRepository repository;
	
	public Category save(Category category) {
		return repository.save(category);
	}
	
	public List<Category> findAll() {
		return repository.findAll();
	}
	
	
	public Category findById(Integer id) {
		return repository.findById(id).get();
	}
	
	public void delete(Integer id) {
		repository.delete(findById(id));
	}
	
	public boolean checkUniqueCategory(Integer id, String name, String alias) {
		
		Category category = findById(id);
		String oldName = category.getName();
		String oldAlias = category.getAlias();
		
		if (!name.equals(oldName)) {
			if (repository.existsCategoryByName(name)) {
				return false;
			}
		}
		
		if(!alias.equals(oldAlias)) {
			if (repository.existsCategoryByAlias(alias)) {
				return false;
			}
		}
		
		return true;
	}
	
	public Page<Category> listByPage(int pageNumber, String keyword) {
		Pageable pageable = PageRequest.of(pageNumber - 1, CATEGORY_PER_PAGE);
		Page<Category> page = (keyword == null) ? 
								repository.findAll(pageable) : repository.findAll(keyword, pageable);
		
		return page;
	}
	
	
}
