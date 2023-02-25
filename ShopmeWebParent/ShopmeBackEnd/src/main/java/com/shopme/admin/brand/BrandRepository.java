package com.shopme.admin.brand;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Integer>{
	public boolean existsByName(String name);
	
	@Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
	public List<Brand> findByKeyword(String keyword);
	
	@Query("SELECT NEW Brand(b.id, b.name) FROM Brand b ORDER BY b.name ASC")
	public List<Brand> findAllBrands();
}
