package com.shopme.setting;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

@Service
public class SettingService {
	
	private SettingRepository repository;
	
	public SettingService(SettingRepository repository) {
		this.repository = repository;
	}
	
	public List<Setting> getGeneralSettings() {
		return repository.findByTwoCategory(SettingCategory.GENERAL, SettingCategory.CURRENCY);
	}
}
