package com.shopme.cart;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class ShoppingCartService {

    private ShoppingCartRepository cartRepository;

    public CartItem save(CartItem cartItem) {

        return cartRepository.save(cartItem);
    }

    public CartItem findByCustomerAndProduct(Customer customer, Product product) {
        return cartRepository.findByCustomerAndProduct(customer, product);
    }

    public CartItem updateCart(Customer customer, Product product, Integer quantity) {
        CartItem cartItem = findByCustomerAndProduct(customer, product);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            CartItem newItems = CartItem.builder()
                    .product(product)
                    .customer(customer)
                    .quantity(quantity)
                    .build();

            CartItem savedItems = cartRepository.save(newItems);

            return  savedItems;
        }

        return cartRepository.save(cartItem);
    }

    public List<CartItem> findItemsByCustomer(Customer customer) {
        List<CartItem> items = cartRepository.findAllByCustomer(customer);

        return items;
    }

    public void deleteProductInCustomerCart(Customer customer, Product product) {
        CartItem item = findByCustomerAndProduct(customer, product);

        cartRepository.delete(item);
    }

    @Transactional
    public void emptyTheCart(Customer customer) {
        cartRepository.deleteByCustomer(customer);
    }
}
