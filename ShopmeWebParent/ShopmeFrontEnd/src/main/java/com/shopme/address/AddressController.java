package com.shopme.address;

import com.shopme.Utility;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerService;
import com.shopme.setting.country.CountryService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class AddressController {
    private AddressService addressService;
    private CustomerService customerService;

    private CountryService countryService;
    private Utility utility;
    @GetMapping("/address_book")
    public String getAddressBook(Model model, Authentication authentication) {
        String customerEmail = utility.getUserEmail(authentication);
        Customer customer = customerService.findByEmail(customerEmail);
        List<Address> addresses = addressService.findAllByCustomer(customer);

        boolean usePrimaryAddressAsDefault = true;
        for (Address address : addresses) {
            if (address.getDefaultAddress()) {
                usePrimaryAddressAsDefault = false;
                break;
            }
        }

        model.addAttribute("listAddresses", addresses);
        model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
        model.addAttribute("customer", customer);

        return "address_book/addresses";
    }

    @GetMapping("/address_book/new")
    public String getAddressForm(Model model) {
        Address address = new Address();

        List<Country> listCountries = countryService.findAll();

        model.addAttribute("address", address);
        model.addAttribute("listCountries", listCountries);

        return "address_book/address_form";
    }

    @PostMapping("/address_book/save")
    public String saveAddressForm(Authentication authentication, Model model, Address address) {
        String customerEmail = utility.getUserEmail(authentication);
        Customer customer = customerService.findByEmail(customerEmail);

        address.setCustomer(customer);
        address.setDefaultAddress(false);
        Address savedAddress = addressService.save(address);

        model.addAttribute("address", savedAddress);

        return "redirect:/address_book";
    }

    @GetMapping("/address_book/default/{id}")
    public String setDefaultAddress(@PathVariable("id") Integer id, Model model,
                                    Authentication authentication) {
        String customerEmail = utility.getUserEmail(authentication);
        Customer customer = customerService.findByEmail(customerEmail);

        List<Address> addresses = addressService.findAllByCustomer(customer);
        addressService.clearDefault(addresses);

         if (id != 0) {
            Address address = addressService.findById(id);
            address.setDefaultAddress(true);
            addressService.save(address);
        }

        return getAddressBook(model, authentication);
    }

    @GetMapping("/address_book/edit/{id}")
    public String editAddress(Model model,
                              @PathVariable("id") Integer id) {
        Address address = addressService.findById(id);
        List<Country> listCountries = countryService.findAll();

        model.addAttribute("address", address);
        model.addAttribute("listCountries", listCountries);

        return "address_book/address_form";
    }

    @GetMapping("/address_book/delete/{id}")
    public String deleteAddress(@PathVariable("id") Integer id,
                                Model model,
                                Authentication authentication) {
        Address address = addressService.findById(id);
        addressService.delete(address);

        return "redirect:/address_book";
    }
}
