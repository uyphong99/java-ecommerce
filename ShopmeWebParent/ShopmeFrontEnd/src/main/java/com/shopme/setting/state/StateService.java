package com.shopme.setting.state;

import com.shopme.common.entity.Country;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.shopme.common.entity.State;
import com.shopme.common.entity.Country;

import java.util.List;

@Service
@AllArgsConstructor
public class StateService {
    private StateRepository stateRepository;

    public List<State> findAllByCountry(Country country) {
        return stateRepository.findAllByCountry(country);
    }
}
