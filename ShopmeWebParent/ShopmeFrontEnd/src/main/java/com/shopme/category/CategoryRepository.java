package com.shopme.category;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
	
	public List<Category> findByEnabled(Boolean enabled);
	
	@Query("SELECT c "
			+ "FROM Category c "
			+ "WHERE c.enabled = true AND c.id NOT IN ( "
			+ "SELECT c.parent FROM Category c "
			+ "WHERE c.parent IS NOT NULL "
			+ ")")
	public List<Category> findAllChildrenCategories();
	
	@Query("SELECT c FROM Category c "
			+ "WHERE c.alias = ?1 AND c.enabled = true")
	public Category findByAliasAndEnabled(String alias);
}
