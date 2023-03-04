package com.shopme.admin.setting;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.Currency;

@Service
public class SettingService {
	private SettingRepository repository;
	
	public SettingService(SettingRepository repository) {
		this.repository = repository;
	}
	
	public String findValueByKey(String key) {
		return repository.findValueByKey(key);
	}
	
	
}
