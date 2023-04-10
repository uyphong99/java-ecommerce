package com.shopme.shippingrate;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ShippingRateRepository extends JpaRepository<ShippingRate, Integer>,
        PagingAndSortingRepository<ShippingRate, Integer> {

    @Query("SELECT r FROM ShippingRate r WHERE CONCAT(r.country.name, r.state) LIKE %?1%")
    Page<ShippingRate> search(String keyword, Pageable pageable);

    Boolean existsByCountryAndState(Country country, String state);

    ShippingRate findByCountryAndState(Country country, String state);
}
