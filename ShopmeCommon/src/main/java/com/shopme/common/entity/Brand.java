package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "brands")
public class Brand {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 128, nullable = false, unique = true)
	private String name;
	
	@Column(length = 64)
	private String logo;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "brands_categories",
			joinColumns= @JoinColumn(name= "brand_id"),
			inverseJoinColumns = @JoinColumn(name = "category_id")
			)
	Set<Category> categories = new HashSet<>();
	
	public Brand() {}
	
	public Brand(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Brand(String name) {
		this.name = name;
		this.logo = "brand-logo.png";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
	
	@Transient
	public String getLogoPath() {
		if (this.id == null) return "/images/image-thumbnail.png";
		
		return "/brands-logos/" + this.id + "/" + this.logo;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
