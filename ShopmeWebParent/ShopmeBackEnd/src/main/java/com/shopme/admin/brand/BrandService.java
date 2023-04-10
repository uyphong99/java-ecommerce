package com.shopme.admin.brand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Brand;

@Service
public class BrandService {
	
	@Autowired
	private BrandRepository brandRepository;
	
	public List<Brand> findAll() {
		return brandRepository.findAll();
	}
	
	public List<Brand> findBrands() {
		return brandRepository.findAllBrands();
	}
	
	public Brand save(Brand brand) {
		Brand savedBrand = brandRepository.save(brand);
		return savedBrand;
	}
	
	public Brand findById(Integer id) {
		Brand brand = brandRepository.findById(id).get();
		
		return brand;
	}
	
	public void deleteBrand(Brand brand) {
		brandRepository.delete(brand);
	}
	
	public boolean checkUnique(String name, Integer id) {

		if (id == null) {
			return !brandRepository.existsByName(name);
		}

		Brand existBrand = findById(id);
		String oldName = existBrand.getName();
		
		if (!oldName.equals(name)) {
			if (brandRepository.existsByName(name)) return false;
		}
		
		return true;
	}
	
	public List<Brand> findByKeyword(String keyword) {
		return brandRepository.findByKeyword(keyword);
	}
}
