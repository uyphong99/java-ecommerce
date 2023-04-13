package com.shopme.checkout;

import com.shopme.cart.ShoppingCartService;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.product.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CheckoutService {

    private static final int DIM_DIVISOR = 139;

    private ShoppingCartService shoppingCartService;

    public CheckoutInfo getInformation(ShippingRate shippingRate, List<CartItem> items) {

        float totalShippingCost = computeShippingCost(items, shippingRate);
        float totalPrice = computeCartItemsPrice(items);
        float productsCost = productCost(items);

        CheckoutInfo info = CheckoutInfo.builder()
                .deliverDays(shippingRate.getDays())
                .paymentTotal(totalShippingCost + totalPrice)
                .deliverDays(shippingRate.getDays())
                .codSupported(shippingRate.isCodSupported())
                .productCost(productsCost)
                .productTotal(totalPrice)
                .shippingCostTotal(totalShippingCost)
                .build();

        return info;
    }

    public float computeShippingCost(List<CartItem> items, ShippingRate rate) {

        float cartShippingCost = 0f;

        for (CartItem item: items) {
            Product product = item.getProduct();

            float w = product.getWidth();
            float h = product.getHeight();
            float l = product.getLength();

            float dimWeight = (w * h * l); // DIM_DIVISOR;

            float weight = Math.max(product.getWeight(), dimWeight);

            float shippingCost = weight * rate.getRate() * item.getQuantity();

            item.setShippingCost(shippingCost);
            shoppingCartService.save(item);

            cartShippingCost += shippingCost;
        }

        return cartShippingCost;
    }

    public float computeCartItemsPrice(List<CartItem> items) {
        float totalPrice = 0f;

        for (CartItem item: items) {
            totalPrice += item.getSubtotal();
        }

        return totalPrice;
    }

    public float productCost(List<CartItem> items) {
        float productsCost = 0f;

        for (CartItem item: items) {
            productsCost += item.getProduct().getCost();
        }

        return productsCost;
    }
}
