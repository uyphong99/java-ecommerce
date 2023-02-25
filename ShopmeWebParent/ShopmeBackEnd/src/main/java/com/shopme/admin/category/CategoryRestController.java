package com.shopme.admin.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {
	
	@Autowired
	private CategoryService categoryService;
	
	@PostMapping("/categories/check_unique")
	public String checkUniqueCategory(@Param("id") Integer id,@Param("name") String name, @Param("alias") String alias) {
		boolean isUnique = categoryService.checkUniqueCategory(id, name, alias);
		
		return isUnique ? "Unique" : "Duplicated";
	}
}
