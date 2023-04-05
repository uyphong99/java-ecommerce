package com.shopme.admin.shippingrate;

import com.shopme.common.entity.ShippingRate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@AllArgsConstructor
public class ShippingRateController {

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
}
