package com.shopme.admin.order;

import com.shopme.admin.DateTimeUtils;
import com.shopme.admin.product.ProductService;
import com.shopme.admin.setting.SettingService;
import com.shopme.admin.setting.country.CountryService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.order.OrderTrack;
import com.shopme.common.entity.product.Product;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.shopme.common.entity.order.Order;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Controller
public class OrderController {
    public static final String defaultRedirectURL = "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";

    private OrderService orderService;
    private SettingService settingService;
    private CountryService countryService;
    private ProductService productService;

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
        List<Country> listCountries = countryService.findAllCountry();
        Order order = orderService.findById(orderId);

        model.addAttribute("pageTitle", "Edit Order (ID: " + orderId + ")");
        model.addAttribute("order", order);
        model.addAttribute("listCountries", listCountries);

        return "orders/order_form";
    }

    @PostMapping("/orders/save")
    public String saveOrder(Order order, HttpServletRequest request, RedirectAttributes ra) {
        String countryName = request.getParameter("countryName");
        order.setCountry(countryName);


        String deliverDateString = request.getParameter("deliverDate");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime deliverDate = LocalDateTime.parse(deliverDateString, formatter);

        order.setDeliverDate(deliverDate);


        updateProductDetails(order, request);
        updateOrderTracks(order, request);

        orderService.save(order);

        ra.addFlashAttribute("message", "The order ID " + order.getId() + " has been updated successfully");

        return "redirect:/orders";
    }

    private void updateOrderTracks(Order order, HttpServletRequest request) {
        String[] trackIds = request.getParameterValues("trackId");
        String[] trackStatuses = request.getParameterValues("trackStatus");
        String[] trackDates = request.getParameterValues("trackDate");
        String[] trackNotes = request.getParameterValues("trackNotes");

        List<OrderTrack> orderTracks = order.getOrderTracks();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        for (int i = 0; i < trackIds.length; i++) {
            OrderTrack trackRecord = new OrderTrack();

            Integer trackId = Integer.parseInt(trackIds[i]);
            if (trackId > 0) {
                trackRecord.setId(trackId);
            }

            trackRecord.setOrder(order);
            trackRecord.setStatus(OrderStatus.valueOf(trackStatuses[i]));
            trackRecord.setNotes(trackNotes[i]);

            trackRecord.setUpdatedTime(LocalDateTime.parse(trackDates[i], dateFormatter));

            orderTracks.add(trackRecord);
        }
    }

    private void updateProductDetails(Order order, HttpServletRequest request) {
        String[] detailIds = request.getParameterValues("detailId");
        String[] productIds = request.getParameterValues("productId");
        String[] productPrices = request.getParameterValues("productPrice");
        String[] productDetailCosts = request.getParameterValues("productDetailCost");
        String[] quantities = request.getParameterValues("quantity");
        String[] productSubtotals = request.getParameterValues("productSubtotal");
        String[] productShipCosts = request.getParameterValues("productShipCost");

        Set<OrderDetail> orderDetails = order.getOrderDetails();

        for (int i = 0; i < detailIds.length; i++) {
            System.out.println("Detail ID: " + detailIds[i]);
            System.out.println("\t Prodouct ID: " + productIds[i]);
            System.out.println("\t Cost: " + productDetailCosts[i]);
            System.out.println("\t Quantity: " + quantities[i]);
            System.out.println("\t Subtotal: " + productSubtotals[i]);
            System.out.println("\t Ship cost: " + productShipCosts[i]);

            OrderDetail orderDetail = new OrderDetail();
            Integer detailId = Integer.parseInt(detailIds[i]);
            if (detailId > 0) {
                orderDetail.setId(detailId);
            }

            orderDetail.setOrder(order);
            orderDetail.setProduct(productService.findById(Integer.parseInt(productIds[i])));
            orderDetail.setProductCost(Float.parseFloat(productDetailCosts[i]));
            orderDetail.setSubtotal(Float.parseFloat(productSubtotals[i]));
            orderDetail.setShippingCost(Float.parseFloat(productShipCosts[i]));
            orderDetail.setQuantity(Integer.parseInt(quantities[i]));
            orderDetail.setUnitPrice(Float.parseFloat(productPrices[i]));

            orderDetails.add(orderDetail);
        }

    }
}
