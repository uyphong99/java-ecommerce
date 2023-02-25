package com.shopme.admin.category;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Category;



public interface CategoryRepository extends JpaRepository<Category, Integer> {
	public boolean existsCategoryByAlias(String alias);
	
	public boolean existsCategoryByName(String name);
	
	@Query("SELECT c FROM Category c WHERE CONCAT(c.name, ' ', c.alias) LIKE %?1%")
	public Page<Category> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
	public List<Category> listRootCategories();
}
