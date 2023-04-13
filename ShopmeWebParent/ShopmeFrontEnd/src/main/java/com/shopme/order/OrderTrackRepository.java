package com.shopme.order;

import com.shopme.common.entity.order.OrderTrack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTrackRepository extends JpaRepository<OrderTrack, Integer> {
}
