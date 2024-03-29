package com.shopme.shippingrate;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

@Service
@AllArgsConstructor
public class ShippingRateService {
    @Autowired
    private ShippingRateRepository shippingRateRepository;

    public static final Integer RATES_PER_PAGE = 10;

    public Page<ShippingRate> listRatePage(Integer pageNumber, String sortField,
                                           String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, RATES_PER_PAGE, sort);

        if (keyword == null) {
            return shippingRateRepository.findAll(pageable);
        }

        return shippingRateRepository.search(keyword, pageable);
    }

    public ShippingRate findById(Integer id) {
        return shippingRateRepository.findById(id).get();
    }

    public ShippingRate save(ShippingRate rate) {
        return shippingRateRepository.save(rate);
    }

    public Boolean existsByCountryAndState(ShippingRate rate) {
        Country country = rate.getCountry();
        String state = rate.getState();
        return shippingRateRepository.existsByCountryAndState(country, state);
    }

    public boolean isUnique(ShippingRate shippingRate) {
        Integer id = shippingRate.getId();

        if (id == null) {
            return !shippingRateRepository.existsByCountryAndState(shippingRate.getCountry(),
                    shippingRate.getState());
        }

        ShippingRate oldRate = findById(id);
        if (shippingRate == null) {
            if (existsByCountryAndState(shippingRate)) {
                return false;
            }
        } else {
            if (!oldRate.isTheSameWith(shippingRate)) {
                if (existsByCountryAndState(shippingRate)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void delete(ShippingRate rate) {
        shippingRateRepository.delete(rate);
    }

    public boolean existsByAddress(Address address) {
        Country country = address.getCountry();
        String state = address.getState();

        return shippingRateRepository.existsByCountryAndState(country, state);
    }

    public ShippingRate findByCountryAndState(Customer customer) {
        Country country = customer.getCountry();
        String state = customer.getState();

        ShippingRate rate = shippingRateRepository.findByCountryAndState(country, state);

        return rate;
    }

    public ShippingRate findByAddress(Address address) {
        Country country = address.getCountry();
        String state = address.getState();

        ShippingRate rate = shippingRateRepository.findByCountryAndState(country, state);

        return rate;
    }
}
