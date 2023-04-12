package com.shopme.checkout;

import com.shopme.Utility;
import com.shopme.address.AddressService;
import com.shopme.cart.ShoppingCartService;
import com.shopme.common.entity.*;
import com.shopme.common.entity.order.Order;
import com.shopme.customer.CustomerService;
import com.shopme.order.OrderService;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;
import com.shopme.shippingrate.ShippingRateService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@AllArgsConstructor
public class CheckoutController {
    private Utility utility;
    private EmailSettingBag settingBag;
    private CustomerService customerService;
    private SettingService settingService;
    private ShoppingCartService shoppingCartService;
    private CheckoutService checkoutService;
    private AddressService addressService;
    private ShippingRateService shippingRateService;
    private OrderService orderService;

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

    @PostMapping("/place_order")
    public String placeOrderAndSendEmail(Authentication authentication, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {

        String customerEmail = utility.getUserEmail(authentication);
        Customer customer = customerService.findByEmail(customerEmail);

        JavaMailSenderImpl mailSender = utility.prepareMailSender();

        String toAddress = customer.getEmail();
        String subject = settingBag.getSubject();
        String content = settingBag.getContent();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(settingBag.getMailFrom(), settingBag.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", customer.getFirstName());
        customerService.resetVerifyCode(customerEmail);
        String verifyURL = Utility.getSiteURL(request) + "/verifyOrder?code=" + customer.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

        return "checkout/order_completed";
    }

    @GetMapping("/verifyOrder")
    public String getOrdersAfterVerify(@RequestParam("code") String verifyCode) {

        Customer customer = customerService.findByVerificationCode(verifyCode);
        Order order = orderService.transformCartToOrder(customer);
        orderService.save(order);
        shoppingCartService.emptyTheCart(customer);

        return "redirect:/";
    }
}
