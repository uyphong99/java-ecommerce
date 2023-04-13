package com.shopme.admin.order;

import com.shopme.admin.setting.SettingService;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import lombok.AllArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.shopme.common.entity.order.Order;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@AllArgsConstructor
@Controller
public class OrderController {
    private OrderService orderService;
    private SettingService settingService;

    @GetMapping("/orders")
    public String getOrderList(Model model) {
        return listByPage(null, 1, "asc", "id", model);
    }

    @GetMapping("/orders/page/{pageNumber}")
    public String listByPage(@Param("keyword") String keyword,
                             @PathVariable("pageNumber") Integer pageNumber,
                             @Param("sortDir") String sortDir,
                             @Param("sortField") String sortField,
                             Model model) {
        Page<Order> orders = orderService.findByKeyword(keyword, pageNumber, sortField, sortDir);
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

        return "orders/orders";
    }

    @GetMapping("/orders/detail/{id}")
    public String showDetail(@PathVariable("id") Integer id, Model model) {
        Order order = orderService.findById(id);
        loadCurrencySetting(model);
        model.addAttribute("order", order);

        return "orders/order_details_modal";
    }

    public void loadCurrencySetting(Model model) {
        List<Setting> settings = settingService.findAllByCategoryIn(SettingCategory.CURRENCY);

        for (Setting setting: settings) {
            model.addAttribute( setting.getKey(), setting.getValue() );
        }
    }

    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable("id") Integer id) {
        Order order = orderService.findById(id);
        orderService.delete(order);

        return "redirect:/orders";
    }

    @GetMapping("/orders/edit/{orderId}")
    public String getOrderEditForm(@PathVariable("orderId") Integer orderId,
                                   Model model) {

        Order order = orderService.findById(orderId);

        model.addAttribute("order", order);

        return "orders/order_form";
    }
}
