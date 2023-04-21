package com.shopme.order;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import com.shopme.common.entity.order.Order;
import com.shopme.customer.CustomerService;
import com.shopme.setting.SettingService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@AllArgsConstructor
@Controller
public class OrderController {
    private OrderService orderService;
    private SettingService settingService;
    private CustomerService customerService;
    private Utility utility;
    @GetMapping("/orders")
    public String getOrderList(Model model, Authentication authentication) {
        return listByPage(null, 1, "asc", "id", model, authentication);
    }

    @GetMapping("/orders/page/{pageNumber}")
    public String listByPage(@Param("keyword") String keyword,
                             @PathVariable("pageNumber") Integer pageNumber,
                             @Param("sortDir") String sortDir,
                             @Param("sortField") String sortField,
                             Model model,
                             Authentication authentication) {
        String customerEmail = utility.getUserEmail(authentication);
        Customer customer = customerService.findByEmail(customerEmail);
        Page<Order> orders = orderService.findByKeyword(keyword, pageNumber, sortField, sortDir, customer);
        List<Order> listOrders = orders.getContent();

        long startCount = (pageNumber - 1) * orderService.ORDER_PER_PAGE + 1;
        long endCount = startCount + orderService.ORDER_PER_PAGE - 1;

        if (endCount > orders.getTotalElements()) {
            endCount = orders.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", orders.getTotalElements());
        model.addAttribute("listOrders", listOrders);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        loadCurrencySetting(model);

        return "orders/orders_customer";
    }

    public void loadCurrencySetting(Model model) {
        List<Setting> settings = settingService.findAllByCategoryIn(SettingCategory.CURRENCY);

        for (Setting setting: settings) {
            model.addAttribute( setting.getKey(), setting.getValue() );
        }
    }
}
