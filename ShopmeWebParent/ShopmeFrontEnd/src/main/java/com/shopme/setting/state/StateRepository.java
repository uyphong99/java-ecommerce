package com.shopme.setting.state;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StateRepository extends CrudRepository<State, Integer> {
    @Query("SELECT s FROM State s WHERE s.country = ?1")
    public List<State> findAllByCountry(Country country);
}
