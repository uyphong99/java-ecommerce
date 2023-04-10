package com.shopme.admin.order;

import com.shopme.common.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o WHERE concat(o.firstName, o.lastName, o.phoneNumber," +
            "o.addressLine1, o.addressLine2, o.postalCode, o.city, o.state, o.country," +
            "o.paymentMethod, o.status, o.customer.firstName, o.customer.lastName) LIKE %?1% ")
    Page<Order> findAllByKeyword(String keyword, Pageable pageable);
}
