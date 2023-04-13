package com.shopme.order;

import com.shopme.common.entity.order.OrderDetail;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderDetailsService {
    private OrderDetailsRepository orderDetailsRepository;

    public OrderDetail save(OrderDetail orderDetail) {
        return orderDetailsRepository.save(orderDetail);
    }
}
