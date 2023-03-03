package com.shopme.product;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.shopme.category.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/c/{category_alias}")
	public String viewCategoryFirstPage(@PathVariable("category_alias") String alias, Model model) {
		return viewCategory(alias, 1, model);
	}
	
	@GetMapping("/c/{category_alias}/page/{pageNum}")
	public String viewCategory(@PathVariable("category_alias") String alias, 
			@PathVariable("pageNum") Integer pageNum,
			Model model) {
		Category category = categoryService.getCategoryByAlias(alias);
		List<Category> listCategoryParents = categoryService.getCategoryParents(category);
		
		Set<Category> subCategories = categoryService.listAllSubcategories(category);
		
		Page<Product> pageProducts =  productService.listByCategory(subCategories, pageNum);
		List<Product> listProducts = pageProducts.getContent();
				
		if (category == null) {
			return "error/404";
		}
		
		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		if (endCount > pageProducts.getTotalElements()) {
			endCount = pageProducts.getTotalElements();
		}
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", pageProducts.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageProducts.getTotalElements());
		model.addAttribute("pageTitle", category.getName());
		model.addAttribute("listCategoryParents", listCategoryParents);
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("category", category);
		
		return "product/products_by_category";
	}
	
	@GetMapping("/p/{alias}")
	public String getProductDetails(@PathVariable("alias") String alias,
				Model model) {
		Product product = productService.findProductByAlias(alias);
		
		model.addAttribute("product", product);
		return "product/product_detail";
	}
	
	@GetMapping("/search")
	public String searchProductsFirstPage(@Param("keyword") String keyword,
				Model model) {
		return searchProduct(keyword, 1, model);
	}
	
	@GetMapping("/search/page/{pageNum}") 
	public String searchProduct(@Param("keyword") String keyword, 
			@PathVariable("pageNum") Integer pageNum,
			Model model){
		Page<Product> pageProducts = productService.findProductByKeyword(keyword, pageNum);
		List<Product> listResult = pageProducts.getContent();
		
		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		
		if (endCount > pageProducts.getTotalElements()) {
			endCount = pageProducts.getTotalElements();
		}
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", pageProducts.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageProducts.getTotalElements());
		model.addAttribute("listResult", listResult);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageTitle", keyword + " - Search Result");
		
		return "product/search_result";
	}
}
