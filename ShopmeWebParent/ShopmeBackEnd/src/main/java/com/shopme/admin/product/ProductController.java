package com.shopme.admin.product;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/products")
	public String findAll(Model model) {
		List<Product> listProducts = service.listAll();
		
		model.addAttribute("listProducts", listProducts);
		
		return "products/products";
	}
	
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		List<Brand> listBrands = brandService.findBrands();
		List<Category> listCategories = categoryService.findAll();
		
		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		
		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Create New Product");
		
		return "products/product_form";
	}
	
	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes redirectAttributes, 
			@RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
		
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		product.setMainImage(fileName);
		
		Product savedProduct = service.save(product);
		String uploadDir = "../product-images/" + savedProduct.getId();
		
		FileUploadUtil.cleanDir(uploadDir);
		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		redirectAttributes.addFlashAttribute("message", "Product has been saved successfully");
		
		return "redirect:/products";
	}
	
	@GetMapping("/products/enable/{id}")
	public String enableProduct(@PathVariable("id") Integer id, 
				RedirectAttributes redirectAttributes) {
		Product product = service.findById(id);
		
		product.setEnabled(!product.isEnabled());
		service.save(product);
		
		return "redirect:/products";
	}
	
	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		service.deleteProductById(id);
		return "redirect:/products";
	}
}
