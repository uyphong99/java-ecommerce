package com.shopme.admin.setting;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.shopme.common.entity.Setting;

@SpringBootTest
public class SettingSpringBootTests {
	
	private SettingController controller;
	
	private SettingService service;
	
	public SettingSpringBootTests(SettingController controller) {
		this.controller = controller;
	}
	
	@Test
	void saveSettingImageTest() {
		Setting setting = service.findByKey("CURRENCY_SYMBOL");
	}
}
