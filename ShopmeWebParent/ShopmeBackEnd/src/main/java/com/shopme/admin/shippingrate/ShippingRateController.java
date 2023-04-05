package com.shopme.admin.shippingrate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShippingRateController {

    @GetMapping("/shipping_rates/page/{pageNumber}")
    public String listByPage(@PathVariable("pageNumber") Integer pageNumber,
                             @RequestParam("keyword") String keyword,
                             Model model) {


        return "shipping_reates";
    }
}
