package com.shopme.common.entity.order;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    //@DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
    @Column(name = "order_time")
    private LocalDateTime orderTime;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "product_cost")
    private Float productCost;

    @Column(name = "shipping_cost")
    private Float shippingCost;

    private Float subtotal;

    private Float tax;

    private Float total;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String firstName;

    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "address_line_1")
    private String addressLine1;
    @Column(name = "address_line_2")
    private String addressLine2;

    private String city;

    private String state;

    @Column(name = "postal_code")
    private String postalCode;

    private String country;

    @Column(name = "deliver_days")
    private Integer deliverDays;
    //@DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
    @Column(name = "deliver_date")
    private LocalDateTime deliverDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDetail> orderDetails = new HashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("updatedTime ASC")
    private List<OrderTrack> orderTracks = new ArrayList<>();


    public void copyAddressFromCustomer() {
        setFirstName(customer.getFirstName());
        setLastName(customer.getLastName());
        setPhoneNumber(customer.getPhoneNumber());
        setAddressLine1(customer.getAddressLine1());
        setAddressLine2(customer.getAddressLine2());
        setCity(customer.getCity());
        setCountry(customer.getCountry().getName());
        setPostalCode(customer.getPostalCode());
        setState(customer.getState());
    }

    @Transient
    public String getDestination() {
        String destination =  city + ", ";
        if (state != null && !state.isEmpty()) destination += state + ", ";
        destination += country;

        return destination;
    }

    @Transient
    public String getUpdatedTimeOnForm() {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        return dateFormatter.format(this.deliverDate);
    }

    public void setUpdatedTimeOnForm(String dateString) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss");

        this.deliverDate = LocalDateTime.parse(dateString, dateFormatter);
    }

    @Transient
    public String getProductNames() {
        String productNames = "";

        productNames = "<ul>";

        for (OrderDetail detail : orderDetails) {
            productNames += "<li>" + detail.getProduct().getShortName() + "</li>";
        }

        productNames += "</ul>";

        return productNames;
    }

    @Transient
    public boolean isCOD() {
        return paymentMethod.equals(PaymentMethod.COD);
    }

    @Transient
    public boolean isProcessing() {
        return hasStatus(OrderStatus.PROCESSING);
    }

    @Transient
    public boolean isPicked() {
        return hasStatus(OrderStatus.PICKED);
    }

    @Transient
    public boolean isShipping() {
        return hasStatus(OrderStatus.SHIPPING);
    }

    @Transient
    public boolean isDelivered() {
        return hasStatus(OrderStatus.DELIVERED);
    }

    @Transient
    public boolean isReturnRequested() {
        return hasStatus(OrderStatus.RETURN_REQUESTED);
    }

    @Transient
    public boolean isReturned() {
        return hasStatus(OrderStatus.RETURNED);
    }

    public boolean hasStatus(OrderStatus status) {
        for (OrderTrack aTrack : orderTracks) {
            if (aTrack.getStatus().equals(status)) {
                return true;
            }
        }

        return false;
    }
}
