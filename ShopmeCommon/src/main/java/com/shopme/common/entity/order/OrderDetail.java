package com.shopme.common.entity.order;

import com.shopme.common.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int quantity;
    @Column(name = "product_cost")
    private float productCost;

    @Column(name = "shipping_cost")
    private float shippingCost;

    @Column(name = "unit_price")
    private float unitPrice;

    private float subtotal;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}