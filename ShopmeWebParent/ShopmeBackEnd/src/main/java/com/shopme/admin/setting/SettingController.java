package com.shopme.admin.setting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.common.entity.Currency;

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
		List<String> nameAttributes = List.of("SITE_LOGO", "SITE_NAME", "COPYRIGHT", "CURRENCY_SYMBOL_POSITION",
				"DECIMAL_POINT_TYPE", "DECIMAL_DIGITS", "THOUSANDS_POINT_TYPE");
		
		addAttributesToModel(model, service, nameAttributes);
		
		List<Currency> listCurrencies = currencyService.findAllCurrencyOrderById();
		model.addAttribute("listCurrencies", listCurrencies);
		
		return "settings/settings";
	}
	
	public void addAttributesToModel(Model model, SettingService service, List<String> strs) {
		for (String str: strs) {
			model.addAttribute(str, service.findValueByKey(str));
		}
	}
}
