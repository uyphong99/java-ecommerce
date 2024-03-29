package com.shopme.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "settings")
public class Setting {
	@Id
	@Column(name = "`key`",nullable = true, length = 128)
	private String key;
	
	@Column(nullable = true, length= 1024)
	private String value;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 45, nullable = false)
	private SettingCategory category;
	
	public Setting() {}

	public Setting(String key, String value, SettingCategory category) {
		this.key = key;
		this.value = value;
		this.category = category;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public SettingCategory getCategory() {
		return category;
	}

	public void setCategory(SettingCategory category) {
		this.category = category;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Setting setting)) return false;

		if (getKey() != null ? !getKey().equals(setting.getKey()) : setting.getKey() != null) return false;
		if (getValue() != null ? !getValue().equals(setting.getValue()) : setting.getValue() != null) return false;
		return getCategory() == setting.getCategory();
	}

	@Override
	public int hashCode() {
		return getKey() != null ? getKey().hashCode() : 0;
	}
}
