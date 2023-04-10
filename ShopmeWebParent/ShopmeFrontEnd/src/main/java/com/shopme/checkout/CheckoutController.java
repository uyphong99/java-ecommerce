package com.shopme.checkout;

import com.shopme.Utility;
import com.shopme.address.AddressService;
import com.shopme.cart.ShoppingCartService;
import com.shopme.common.entity.*;
import com.shopme.customer.CustomerService;
import com.shopme.setting.SettingService;
import com.shopme.shippingrate.ShippingRateService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@AllArgsConstructor
public class CheckoutController {
    private Utility utility;
    private CustomerService customerService;
    private SettingService settingService;
    private ShoppingCartService shoppingCartService;
    private CheckoutService checkoutService;
    private AddressService addressService;
    private ShippingRateService shippingRateService;
    @GetMapping("/checkout")
    public String getCheckoutPage(Model model, Authentication authentication) {
        String customerEmail = utility.getUserEmail(authentication);
        Customer customer = customerService.findByEmail(customerEmail);

        List<CartItem> items = shoppingCartService.findItemsByCustomer(customer);
        Address defaultAddress = addressService.findCustomerDefaultAddress(customer);

        ShippingRate rate = shippingRateService.findByCountryAndState(customer);
        CheckoutInfo checkoutInfo = checkoutService.getInformation(rate, items);

        String shippingAddress = defaultAddress.toString();

        model.addAttribute("customer", customer);
        model.addAttribute("checkoutInfo", checkoutInfo);
        model.addAttribute("cartItems", items);
        model.addAttribute("shippingAddress", shippingAddress);

        loadCurrencySetting(model);

        return "checkout/checkout";
    }

    public void loadCurrencySetting(Model model) {
        List<Setting> settings = settingService.findAllByCategoryIn(SettingCategory.CURRENCY);

        for (Setting setting: settings) {
            model.addAttribute( setting.getKey(), setting.getValue() );
        }
    }
}
