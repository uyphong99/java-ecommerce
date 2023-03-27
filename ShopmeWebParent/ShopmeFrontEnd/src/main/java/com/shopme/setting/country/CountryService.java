package com.shopme.setting.country;

import com.shopme.common.entity.Country;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CountryService {
    private CountryRepository repository;

    public List<Country> findAll() {
        return (List<Country>) repository.findAll();
    }
}
