package com.shopme;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.category.CategoryService;
import com.shopme.common.entity.Category;

@Controller
public class MainController {
	
	@Autowired
	private CategoryService service;
	
	@GetMapping("/")
	public String viewHomePage(Model model) {
		
		List<Category> listCategories = service.listNoChildrenCategories();
		
		model.addAttribute("listCategories", listCategories);
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}
}
