package com.shopme.admin.brand;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@Controller
public class BrandController {
	
	@Autowired
	private BrandService service;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/brands")
	public String listAllBrands(Model model, @Param("keyword") String keyword) {
		
		List<Brand> listBrands;
		
		if (keyword == null) {
			listBrands = service.findAll();
		} else {
			listBrands = service.findByKeyword(keyword);
		}
		
		model.addAttribute("listBrands", listBrands);
		
		return "brands/brands";
	}
	
	@GetMapping("brands/new")
	public String updateBrands(Model model) {
		List<Category> categories = categoryService.findAll();
		
		Brand brand = new Brand();
		model.addAttribute("brand", brand);
		model.addAttribute("categories", categories);
		
		return "brands/brand_form";
	}
	
	@PostMapping("/brands/save")
	public String saveNewBrand(Brand brand, RedirectAttributes redirectAttributes,
				@RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
		
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		brand.setLogo(fileName);
		
		Brand savedBrand =  service.save(brand);
		String uploadDir = "../brands-logos/" + savedBrand.getId();
		
		FileUploadUtil.cleanDir(uploadDir);
		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		redirectAttributes.addFlashAttribute("message", "The brand " + brand.getName() + " has been saved successfully.");
		return "redirect:/brands";
	}
	
	@GetMapping("/brands/edit/{brandId}")
	public String updateBrand(@PathVariable("brandId") Integer id,
				Model model) {
		Brand updatedBrand = service.findById(id);
		List<Category> categories = categoryService.findAll();
		
		model.addAttribute("categories", categories);
		model.addAttribute("brand", updatedBrand);
		
		return "brands/brand_form";
	}
	
	@GetMapping("/brands/delete/{brandId}")
	public String deleteBrand(@PathVariable("brandId") Integer id, 
				RedirectAttributes redirectAttributes) {
		Brand brand = service.findById(id);
		redirectAttributes.addFlashAttribute("message", "Brand " + brand.getName() + " has been deleted.");
		
		service.deleteBrand(brand);
		
		return "redirect:/brands";
	}
}
