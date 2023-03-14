package com.shopme.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class SettingRepositoryTests {
	
	private SettingRepository repository;
	
	public SettingRepositoryTests(@Autowired SettingRepository settingRepository) {
		this.repository = settingRepository;
	}
	
	@Test
	void testFindByCategories() {
		List<Setting> settings = repository.findByTwoCategory(SettingCategory.GENERAL, SettingCategory.CURRENCY);
		
		assertThat(settings.size()).isGreaterThan(0);
	}
}
