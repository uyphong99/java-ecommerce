package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
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


    public Boolean isTheSameWith(ShippingRate rate) {
        String otherCountryName = rate.country.getName();
        String thisCountryName = this.country.getName();

        String otherState = rate.getState();
        String thisState = this.getState();

        if (otherCountryName.equals(thisCountryName) && otherState.equals(thisState)) {
            return true;
        }

        return false;
    }
}
