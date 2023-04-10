package com.shopme.setting;

import java.util.List;
import java.util.Set;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

public interface SettingRepository extends JpaRepository<Setting, Integer> {
	
	@Query("SELECT s FROM Setting s WHERE s.category = ?1 OR s.category = ?2")
	public List<Setting> findByTwoCategory(SettingCategory catOne, SettingCategory catTwo);
	
	public List<Setting> findByCategory(SettingCategory category);

	@Query("SELECT s.value FROM Setting s WHERE s.key = ?1")
	public String findValueByKey(String key);

	public Set<Setting> findAllByCategoryContains(SettingCategory... categories);

	List<Setting> findByCategoryIn(SettingCategory... categories);
}
