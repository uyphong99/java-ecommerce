package com.shopme.admin.setting;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.Setting;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SettingController {
	private SettingService service;
	
	private CurrencyService currencyService;
	
	public SettingController(SettingService service, CurrencyService currencyService) {
		this.service = service;
		this.currencyService = currencyService;
	}
	
	@GetMapping("/settings")
	public String getSettingPage(Model model) {
		
		List<Setting> settings = service.findAllSettings();
		
		addAttributesToModel(model, settings);
		
		List<Currency> listCurrencies = currencyService.findAllCurrencyOrderById();
		model.addAttribute("listCurrencies", listCurrencies);
		
		return "settings/settings";
	}
	
	public void addAttributesToModel(Model model, List<Setting> settings) {
		for (Setting setting: settings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}
	}
	
	@PostMapping("/settings/save_general")
	public String saveSettings(RedirectAttributes redirectAttributes,
				@RequestParam("fileImage") MultipartFile multipartFile,
				HttpServletRequest request) throws IOException {
		
		List<Setting> settings = service.findAllSettings();
		Setting logoSetting = service.findByKey("SITE_LOGO");
		
		saveAllSettings(settings, request);
		saveCurrencySymbol();
		
		if (!multipartFile.isEmpty()) {
			saveSiteLogo(multipartFile, logoSetting);
		}
		
		redirectAttributes.addFlashAttribute("message", "Saved settings");
		
		return "redirect:/settings";
	}
	
	public void saveAllSettings(List<Setting> settings, HttpServletRequest request) {
		for (Setting setting: settings) {
			if (!setting.getKey().equals("SITE_LOGO") || !setting.getKey().equals("CURRENCY_SYMBOL")) {
				String value = request.getParameter(setting.getKey());
				setting.setValue(value);
				service.save(setting);
			}
		}
		
	}
	
	public void saveCurrencySymbol() {
		Setting curencyId = service.findByKey("CURRENCY_ID");
		Integer currencyIdInteger = Integer.parseInt(curencyId.getValue());
		Setting currencySymbol = service.findByKey("CURRENCY_SYMBOL");
		currencySymbol.setValue(currencyService.findSymbolById(currencyIdInteger));
		service.save(currencySymbol);
	}
	
	public void saveSiteLogo(MultipartFile multipartFile, Setting logo) throws IOException {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		
		String uploadDir = "../site-logo/";
		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		
		logo.setValue("ShopmeAdminSmall.png");
		service.save(logo);
		
		FileUploadUtil.changeImageName(uploadDir, fileName, "ShopmeAdminSmall.png"); //replace exist image in folder
	}
}
