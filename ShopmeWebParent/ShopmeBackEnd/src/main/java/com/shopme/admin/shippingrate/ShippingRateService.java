package com.shopme.admin.shippingrate;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ShippingRateService {
    @Autowired
    private ShippingRateRepository shippingRateRepository;

    public static final Integer RATES_PER_PAGE = 10;
}
