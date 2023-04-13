package com.shopme.order;

import com.shopme.common.entity.order.OrderTrack;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
@AllArgsConstructor
@Service
public class OrderTrackService {
    private OrderTrackRepository orderTrackRepository;

    public OrderTrack save(OrderTrack orderTrack) {
        return orderTrackRepository.save(orderTrack);
    }
}
