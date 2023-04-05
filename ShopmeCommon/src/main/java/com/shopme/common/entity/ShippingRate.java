package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "shipping_rates")
public class ShippingRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Float rate;

    private Integer days;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    private String state;

    @Column(name = "cod_supported")
    private boolean codSupported;
}
