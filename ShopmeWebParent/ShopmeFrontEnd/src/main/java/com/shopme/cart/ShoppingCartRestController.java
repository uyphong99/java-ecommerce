package com.shopme.cart;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;
import com.shopme.customer.CustomerService;
import com.shopme.product.ProductService;
import com.shopme.security.CustomerUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
@AllArgsConstructor
@RestController
public class ShoppingCartRestController {

    private ProductService productService;

    private ShoppingCartService cartService;

    private CustomerService customerService;

    @PostMapping("/cart/add/{productId}/{quantity}")
    public String addProductToCart(@PathVariable("productId") Integer productId,
                                   @PathVariable("quantity") Integer quantity,
                                   Authentication authentication) {
        Product product = productService.findById(productId);
        CustomerUserDetails customerUserDetails = (CustomerUserDetails) authentication.getPrincipal();

        String customerEmail = customerUserDetails.getUsername();
        Customer customer = customerService.findByEmail(customerEmail);

        CartItem cart = cartService.updateCart(customer, product, quantity);

        return quantity + " item(s) were added to cart! ";
    }
}
