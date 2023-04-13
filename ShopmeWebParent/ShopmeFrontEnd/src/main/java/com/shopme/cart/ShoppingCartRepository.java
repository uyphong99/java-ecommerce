package com.shopme.cart;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ShoppingCartRepository extends CrudRepository<CartItem, Integer> {
    CartItem findByCustomerAndProduct(Customer customer, Product product);

    List<CartItem> findAllByCustomer(Customer customer);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.customer = :customer")
    void deleteByCustomer(Customer customer);
}
