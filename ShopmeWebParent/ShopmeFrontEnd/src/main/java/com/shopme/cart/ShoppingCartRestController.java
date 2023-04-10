package com.shopme.cart;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import com.shopme.customer.CustomerService;
import com.shopme.product.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.shopme.Utility;
@AllArgsConstructor
@RestController
public class ShoppingCartRestController {

    private ProductService productService;

    private ShoppingCartService cartService;

    private CustomerService customerService;

    private Utility utility;

    @PostMapping("/cart/add/{productId}/{quantity}")
    public String addProductToCart(@PathVariable("productId") Integer productId,
                                   @PathVariable("quantity") Integer quantity,
                                   Authentication authentication) {

        Product product = productService.findById(productId);
        String customerEmail = utility.getUserEmail(authentication);

        Customer customer = customerService.findByEmail(customerEmail);

        cartService.updateCart(customer, product, quantity);

        return quantity + " item(s) were added to cart! ";
    }

    @PostMapping("/cart/update/{productId}/{quantity}")
    public String updateProductQuantity(@PathVariable("productId") Integer productId,
                                      @PathVariable("quantity") Integer quantity,
                                      Authentication authentication) {
        String customerEmail = utility.getUserEmail(authentication);
        Customer customer = customerService.findByEmail(customerEmail);
        Product product = productService.findById(productId);

        CartItem item = cartService.findByCustomerAndProduct(customer, product);
        item.setQuantity(quantity);

        cartService.save(item);

        Float subtotal = item.getSubtotal();

        return String.valueOf(subtotal);
    }

    @DeleteMapping("cart/remove/{productId}")
    public String deleteCartItem(@PathVariable("productId") Integer productId,
                                 Authentication authentication) {

        String customerEmail = utility.getUserEmail(authentication);
        Customer customer = customerService.findByEmail(customerEmail);
        Product product = productService.findById(productId);

        cartService.deleteProductInCustomerCart(customer, product);

        return "Product " + productId + " has been deleted from your cart!";
    }
}
