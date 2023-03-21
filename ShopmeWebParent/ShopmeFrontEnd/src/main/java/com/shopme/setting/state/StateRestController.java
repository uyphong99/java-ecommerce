package com.shopme.setting.state;

import com.shopme.common.entity.Country;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class StateRestController {
    private StateService stateService;

    @GetMapping("/settings/list_states_by_country/{country}")
    public List<StateDTO> findAllByCountry(@PathVariable("country") Country country) {
        return stateService.findAllByCountry(country).stream()
                .map(state -> new StateDTO(state))
                .toList();
    }
}
