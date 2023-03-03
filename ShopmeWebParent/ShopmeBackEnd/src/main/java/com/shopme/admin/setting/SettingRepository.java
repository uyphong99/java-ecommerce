package com.shopme.admin.setting;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopme.common.entity.Setting;

public interface SettingRepository extends JpaRepository<Setting, String>{

}
