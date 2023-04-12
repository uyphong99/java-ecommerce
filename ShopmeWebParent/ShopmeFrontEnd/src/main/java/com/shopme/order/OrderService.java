package com.shopme.order;

import com.shopme.address.AddressService;
import com.shopme.cart.ShoppingCartService;
import com.shopme.checkout.CheckoutService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.order.PaymentMethod;
import com.shopme.shippingrate.ShippingRateService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class OrderService {
    private OrderRepository orderRepository;
    private CheckoutService checkoutService;
    private ShoppingCartService shoppingCartService;
    private AddressService addressService;
    private ShippingRateService shippingRateService;
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

    public OrderDetail mapItemToOrderDetail(CartItem item) {
        return OrderDetail.builder()
                .quantity(item.getQuantity())
                .shippingCost(item.getShippingCost())
                .subtotal(item.getSubtotal())
                .unitPrice(item.getProduct().getPrice())
                .product(item.getProduct())
                .build();

    }

    public Set<OrderDetail> mapCartToOrderDetails(List<CartItem> items) {
        return items.stream()
                .map(this::mapItemToOrderDetail)
                .collect(Collectors.toSet());
    }

    public Order transformCartToOrder(Customer customer) {
        Address address = addressService.findCustomerDefaultAddress(customer);
        ShippingRate rate = shippingRateService.findByAddress(address);
        List<CartItem> items = shoppingCartService.findItemsByCustomer(customer);

        return Order.builder()
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .orderDetails(mapCartToOrderDetails(items))
                .country(address.getCountry().getName())
                .orderTime(LocalDateTime.now())
                .deliverDays(rate.getDays())
                .firstName(address.getFirstName())
                .lastName(address.getLastName())
                .city(address.getCity())
                .postalCode(address.getPostalCode())
                .state(address.getState())
                .productCost(checkoutService.productCost(items))
                .tax(0.1f)
                .deliverDate(LocalDateTime.now().plusDays(rate.getDays()))
                .phoneNumber(address.getPhoneNumber())
                .shippingCost(checkoutService.computeShippingCost(items, rate))
                .total(checkoutService.computeShippingCost(items, rate)
                        + checkoutService.productCost(items))
                .status(OrderStatus.NEW)
                .paymentMethod(PaymentMethod.COD)
                .customer(customer)
                .build();
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }
}
