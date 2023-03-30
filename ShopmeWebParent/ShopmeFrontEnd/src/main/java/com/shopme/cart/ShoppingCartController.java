package com.shopme.cart;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShoppingCartController {

    @GetMapping("/cart")
    public String getCustomerShoppingCart() {
        return "cart/shopping_cart";
    }
}
