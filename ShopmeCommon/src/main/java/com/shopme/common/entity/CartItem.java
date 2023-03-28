package com.shopme.common.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;


}
