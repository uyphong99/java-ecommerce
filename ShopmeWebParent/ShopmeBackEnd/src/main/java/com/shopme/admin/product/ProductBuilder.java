package com.shopme.admin.product;

import java.time.LocalDateTime;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;


public class ProductBuilder {
	private Integer id;
	private String name;
	
	private String alias;
	
	private String shortDescription;
	
	private String fullDescription;
	
	private LocalDateTime createdTime;
	
	private LocalDateTime updatedTime;
	
	private boolean enabled;
	private boolean inStock;
	
	private float cost;
	
	private float price;
	
	private float discountPercent;
	
	private float length;
	private float width;
	private float height;
	private float weight;

	private Category category;
	
	private Brand brand;
	private String mainImage;

	public ProductBuilder setId(Integer id) {
		this.id = id;
		return this;
	}

	public ProductBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public ProductBuilder setAlias(String alias) {
		this.alias = alias;
		return this;
	}

	public ProductBuilder setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
		return this;
	}

	public ProductBuilder setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
		return this;
	}

	public ProductBuilder setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
		return this;
	}

	public ProductBuilder setUpdatedTime(LocalDateTime updatedTime) {
		this.updatedTime = updatedTime;
		return this;
	}

	public ProductBuilder setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public ProductBuilder setInStock(boolean inStock) {
		this.inStock = inStock;
		return this;
	}

	public ProductBuilder setCost(float cost) {
		this.cost = cost;
		return this;
	}

	public ProductBuilder setPrice(float price) {
		this.price = price;
		return this;
	}

	public ProductBuilder setDiscountPercent(float discountPercent) {
		this.discountPercent = discountPercent;
		return this;
	}

	public ProductBuilder setLength(float length) {
		this.length = length;
		return this;
	}

	public ProductBuilder setWidth(float width) {
		this.width = width;
		return this;
	}

	public ProductBuilder setHeight(float height) {
		this.height = height;
		return this;
	}

	public ProductBuilder setWeight(float weight) {
		this.weight = weight;
		return this;
	}

	public ProductBuilder setCategory(Category category) {
		this.category = category;
		return this;
	}

	public ProductBuilder setBrand(Brand brand) {
		this.brand = brand;
		return this;
	}
	
	public ProductBuilder setMainImage(String image) {
		this.mainImage = image;
		return this;
	}
	
	public Product build() {
		return new Product( id,  name,  alias,  shortDescription,  fullDescription,
				 createdTime,  updatedTime,  enabled,  inStock,  cost,  price,
				 discountPercent,  length,  width,  height,  weight,  category,
				 brand, mainImage);
	}
}
