package com.shopme.cart;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;
import org.springframework.data.repository.CrudRepository;

public interface ShoppingCartRepository extends CrudRepository<CartItem, Integer> {
    CartItem findByCustomerAndProduct(Customer customer, Product product);
}
