package com.shopme.admin.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {
	
	@Autowired
	private ProductService service;
	
	@PostMapping("/products/check_unique")
	public String checkUnique(@Param("name") String name, @Param("id") Integer id) {
		return service.checkUnique(name, id) ? "Unique" : "Duplicated";
	}
}
