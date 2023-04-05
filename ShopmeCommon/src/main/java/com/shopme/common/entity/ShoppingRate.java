package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Float rate;

    private Integer days;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    private String state;
}
