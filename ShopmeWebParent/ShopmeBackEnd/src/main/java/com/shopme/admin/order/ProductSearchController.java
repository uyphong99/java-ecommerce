package com.shopme.admin.order;

import com.shopme.admin.customer.CustomerService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.product.ProductService;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;


import java.util.List;

@Controller
@AllArgsConstructor
public class ProductSearchController {

	private ProductService service;
	
	@GetMapping("/orders/search_product")
	public String showSearchProductPage() { //model

		//List<Product> listProducts = service.listAll();

		//model.addAttribute("listProducts", listProducts);

		return "orders/search_product";
	}
	
	@PostMapping("/orders/search_product")
	public String searchProducts(String keyword) {
		return "redirect:/orders/search_product/page/1?sortField=name&sortDir=asc&keyword=" + keyword;
	}
	/*
	@GetMapping("/orders/search_product/page/{pageNum}")
	public String searchProductsByPage(@PagingAndSortingParam(listName = "listProducts", 
			moduleURL = "/orders/search_product") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum) {
		service.searchProducts(pageNum, helper);
		return "orders/search_product";
	}

	 */

	@GetMapping("/orders/search_product/page/{pageNum}")
	public String customerPage(@Param("keyword") String keyword,
							   @PathVariable("pageNumber") Integer pageNumber,
							   @Param("sortDir") String sortDir,
							   @Param("sortField") String sortField,
							   Model model) {
		Page<Product> pageProduct = service.listProducts(pageNumber, sortField, sortDir, keyword);
		List<Product> listProducts = pageProduct.getContent();

		long startCount = (pageNumber - 1) * CustomerService.CUSTOMERS_PER_PAGE + 1;
		long endCount = startCount + CustomerService.CUSTOMERS_PER_PAGE - 1;

		if (endCount > pageProduct.getTotalElements()) {
			endCount = pageProduct.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("totalPages", pageProduct.getTotalPages());
		model.addAttribute("currentPage", pageNumber);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageProduct.getTotalElements());
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);

		return "orders/search_product";
	}
}
