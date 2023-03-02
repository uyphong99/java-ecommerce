package com.shopme.category;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	
	public List<Category> getCategoryParents(Category childCategory) {
		List<Category> listParent = new ArrayList<>();
		
		Category parentCategory = childCategory.getParent();
		
		while (parentCategory != null) {
			listParent.add(0, parentCategory);
			parentCategory = parentCategory.getParent();
		}
		
		listParent.add(childCategory);
		
		return listParent;
	}
	
	public Category getCategoryByAlias(String alias) {
		return repository.findByAliasAndEnabled(alias);
	}
	
	public Set<Category> listAllSubcategories(Category category) {
        Set<Category> set = new HashSet<>();
        set.add(category);

        Set<Category> subCategories = category.getChildren();

        if (subCategories.size() != 0) {
            for (Category cat: subCategories) {
                set.addAll(listAllSubcategories(cat));
            }
        }

        return set;
    }
}
