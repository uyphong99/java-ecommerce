package com.shopme.admin.order;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.order.Order;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private OrderRepository orderRepository;

    public static final Integer ORDER_PER_PAGE = 10;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Integer id) {
        return orderRepository.findById(id).get();
    }

    public Page<Order> findByKeyword(String keyword, Integer pageNumber,
                                     String sortField, String sortDir) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, ORDER_PER_PAGE, sort);

        if (keyword == null) {
            return orderRepository.findAll(pageable);
        }

        return orderRepository.findAllByKeyword(keyword, pageable);
    }

    public void delete(Order order) {
        orderRepository.delete(order);
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }
}
