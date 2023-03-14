package com.shopme.admin.setting;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.Setting;

@Service
public class SettingService {
	private SettingRepository repository;
	
	public SettingService(SettingRepository repository) {
		this.repository = repository;
	}
	
	public String findValueByKey(String key) {
		return repository.findValueByKey(key);
	}
	
	public List<Setting> findAllSettings() {
		return repository.findAll();
	}
	
	public void save(Setting setting) {
		repository.save(setting);
	}
	
	public Setting findByKey(String key) {
		return repository.findByKey(key);
	}
}
