package com.shopme.customer;

import com.shopme.Utility;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.security.oauth.CustomerOauth2User;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.country.CountryService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@AllArgsConstructor
public class CustomerController {
    private CustomerService service;

    private CountryService countryService;
    private EmailSettingBag settingBag;
    private Utility utility;

    @GetMapping("/register")
    public String registerCustomer(Model model) {
        Customer customer = new Customer();
        List<Country> listCountries = service.listAllCountry();

        model.addAttribute("listCountries", listCountries);
        model.addAttribute("customer", customer);
        model.addAttribute("pageTitle", "Customer Registration");
        return "register/register_form";
    }

    @PostMapping("/create_customer")
    public String saveCustomer(Customer customer, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        Customer savedCustomer = service.save(customer);
        sendVerificationEmail(request, customer);

        return "register/register_success";
    }

    private void sendVerificationEmail(HttpServletRequest request, Customer customer)
        throws UnsupportedEncodingException, MessagingException {
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

        String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("code") String verificationCode) {
        if (service.existsByVerificationCode(verificationCode)) {
            Customer customer = service.findByVerificationCode(verificationCode);

            customer.setEnabled(true);
        }
        return "redirect:/login";
    }

    @GetMapping("/account_details")
    public String viewCustomerDetail(Authentication authentication, Model model) {
        CustomerOauth2User customerOauth2User = (CustomerOauth2User) authentication.getPrincipal();
        String email = customerOauth2User.getEmail();
        Customer customer = service.findByEmail(email);

        List<Country> listCountries = countryService.findAll();

        model.addAttribute("listCountries", listCountries);
        model.addAttribute("customer", customer);

        return "customer/account_form";
    }

    @PostMapping("/update_account_details")
    public String updateCustomerAccount(Customer customer) {
        service.save(customer);

        return "redirect:/account_details";
    }

}
