package com.shopme.admin.shippingrate;

import com.shopme.admin.setting.country.CountryService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class ShippingRateController {
    private CountryService countryService;
    private ShippingRateService shippingRateService;

    @GetMapping("/shipping_rates")
    public String listFirstPage(Model model) {
        return listByPage(null, 1, "asc", "id", model);
    }

    @GetMapping("/shipping_rates/page/{pageNumber}")
    public String listByPage(@Param("keyword") String keyword,
                             @PathVariable("pageNumber") Integer pageNumber,
                             @Param("sortDir") String sortDir,
                             @Param("sortField") String sortField,
                             Model model) {
        Page<ShippingRate> shippingRates = shippingRateService.listRatePage(pageNumber, sortField, sortDir, keyword);
        List<ShippingRate> rates = shippingRates.getContent();

        long startCount = (pageNumber - 1) * ShippingRateService.RATES_PER_PAGE + 1;
        long endCount = startCount + ShippingRateService.RATES_PER_PAGE - 1;

        if (endCount > shippingRates.getTotalElements()) {
            endCount = shippingRates.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("totalPages", shippingRates.getTotalPages());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", shippingRates.getTotalElements());
        model.addAttribute("shippingRates", rates);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return "shipping_rates/shipping_rates";
    }

    @GetMapping("/shipping_rates/cod/{id}/enabled/{enabled}")
    public String enableCodSupported(@PathVariable("id") Integer id,
                                     @PathVariable("enabled") String enabledString) {
        Boolean enabled = enabledString.equals("true") ? true : false;

        ShippingRate rate = shippingRateService.findById(id);
        rate.setCodSupported(enabled);
        shippingRateService.save(rate);

        return "redirect:/shipping_rates";
    }

    @GetMapping("/shipping_rates/new")
    public String shippingRateForm(Model model) {

        ShippingRate rate = new ShippingRate();
        List<Country> listCountries = countryService.findAllCountry();

        model.addAttribute("rate", rate);
        model.addAttribute("listCountries", listCountries);


        return "shipping_rates/shipping_rate_form";
    }

    @PostMapping("/shipping_rates/save")
    public String saveRate(ShippingRate shippingRate, Model model) {
        if (shippingRateService.isUnique(shippingRate)) {
            shippingRateService.save(shippingRate);
        }

        return "redirect:/shipping_rates/new";
    }

    @GetMapping("/shipping_rates/edit/{id}")
    public String editForm(@PathVariable("id") Integer id, Model model) {

        ShippingRate rate = shippingRateService.findById(id);
        List<Country> listCountries = countryService.findAllCountry();

        model.addAttribute("rate", rate);
        model.addAttribute("listCountries", listCountries);

        return "shipping_rates/shipping_rate_form";
    }

    @GetMapping("/shipping_rates/delete/{id}")
    public String deleteShippingRate(@PathVariable("id") Integer id) {
        ShippingRate rate = shippingRateService.findById(id);
        shippingRateService.delete(rate);

        return "redirect:/shipping_rates";
    }
}
