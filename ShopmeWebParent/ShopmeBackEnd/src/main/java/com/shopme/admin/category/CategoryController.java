package com.shopme.admin.category;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Category;

@Controller
public class CategoryController {
	
	@Autowired
	CategoryService service;
	
	@GetMapping("/categories")
	public String categoryListPage(Model model) {
		
		return listByPage(1, null, model);
	}
	
	@GetMapping("/categories/page/{pageNumber}")
	public String listByPage(@PathVariable("pageNumber") int pageNumber,
			@Param("keyword") String keyword, Model model) {
		Page<Category> pageCategory = service.listByPage(pageNumber, keyword);
		List<Category> listCategories = pageCategory.getContent();
		
		long startCount = (pageNumber - 1) * CategoryService.CATEGORY_PER_PAGE + 1;
		long endCount = startCount + CategoryService.CATEGORY_PER_PAGE - 1;
		
		if (endCount > pageCategory.getTotalElements()) {
			endCount = pageCategory.getTotalElements();
		} 
		
		model.addAttribute("totalPages", pageCategory.getTotalPages());
		model.addAttribute("currentPage", pageNumber);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageCategory.getTotalElements());
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("keyword", keyword);
		
		return "categories/categories";
	}
	
	@GetMapping("/categories/enable/{categoryId}")
	public String enableCategory(@PathVariable("categoryId") Integer id) {
		Category category = service.findById(id);
		category.setEnabled(!category.getEnabled());
		service.save(category);
		
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/delete/{categoriesId}")
	public String deleteCategory(@PathVariable("categoriesId") Integer id) {
		service.delete(id);
		
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/new")
	public String addCategory(Model model) {
		List<Category> listCategories = service.findAll();
		
		model.addAttribute("category", new Category());
		model.addAttribute("pageTitle", "Create new category");
		model.addAttribute("listCategories", listCategories);

		return "categories/category_form";
	}
	
	@PostMapping("/categories/save")
	public String saveCategory(@RequestParam("fileImage") MultipartFile multipartFile,
			Category category, RedirectAttributes redirectAttributes) throws IOException {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		
		category.setImage(fileName);
		Category savedCategory = service.save(category);
		String uploadDir = "../category-images/" + savedCategory.getId();
		
		FileUploadUtil.cleanDir(uploadDir);
		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		
		redirectAttributes.addFlashAttribute("message", "The category has been saved successfully.");

		return "redirect:/categories";
	}
	
	@GetMapping("/categories/update/{categoryId}")
	public String updateCategory(@PathVariable("categoryId") Integer categoryId, 
				Model model) {
		Category category = service.findById(categoryId);
		model.addAttribute("category", category);
		
		return "categories/category_form";
	}
}
