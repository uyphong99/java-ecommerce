package com.shopme.cart;

import com.shopme.Utility;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
@AllArgsConstructor
@Controller
public class ShoppingCartController {

    private Utility utility;
    private ShoppingCartService cartService;

    private CustomerService customerService;

    @GetMapping("/cart")
    public String getCustomerShoppingCart(Authentication authentication,
                                          Model model) {
        String customerEmail = utility.getUserEmail(authentication);
        Customer customer = customerService.findByEmail(customerEmail);
        List<CartItem> cartItemList = cartService.findItemsByCustomer(customer);

        float estimatedTotal = 0.0F;

        for (CartItem item : cartItemList) {
            estimatedTotal += item.getSubtotal();
        }

        model.addAttribute("cartItems", cartItemList);
        model.addAttribute("estimatedTotal", estimatedTotal);


        return "cart/shopping_cart";
    }
}
