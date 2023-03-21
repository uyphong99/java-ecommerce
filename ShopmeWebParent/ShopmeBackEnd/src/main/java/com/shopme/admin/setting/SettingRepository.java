package com.shopme.admin.setting;

import java.util.List;

import com.shopme.common.entity.SettingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Setting;

public interface SettingRepository extends JpaRepository<Setting, String>{
	
	@Query("SELECT s.value FROM Setting s WHERE s.key = ?1")
	public String findValueByKey(String key);
	
	@Query("SELECT s FROM Setting s WHERE s.key =?1")
	public Setting findByKey(String key);

	List<Setting> findByCategoryIn(SettingCategory... categories);
}
